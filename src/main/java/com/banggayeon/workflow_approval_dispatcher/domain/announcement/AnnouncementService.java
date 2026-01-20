package com.banggayeon.workflow_approval_dispatcher.domain.announcement;

import com.banggayeon.workflow_approval_dispatcher.domain.announcement.client.GeneratedAnnouncement;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.client.OpenAiClient;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto.AnnouncementCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto.AnnouncementUpdateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.template.Template;
import com.banggayeon.workflow_approval_dispatcher.domain.template.TemplateRepository;
import com.banggayeon.workflow_approval_dispatcher.global.error.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnnouncementService {
    private final AnnouncementRepository repo;
    private final TemplateRepository templateRepo;
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public AnnouncementService(
        AnnouncementRepository repo,
        TemplateRepository templateRepo,
        OpenAiClient openAiClient,
        ObjectMapper objectMapper
    ){
        this.repo = repo;
        this.templateRepo = templateRepo;
        this.openAiClient = openAiClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Long create(AnnouncementCreateRequest req){
        templateRepo.findById(req.getTemplateId())
                .orElseThrow(() -> new NotFoundException("Template not found: " + req.getTemplateId()));

        String keywordsJson = toJson(req.getRequiredKeywords());
        Announcement a = new Announcement(
            req.getTemplateId(),
            req.getRunAt(),
            keywordsJson,
            req.getTone(),
            req.getDiscordWebhookUrl(),
            req.getCreatedBy()
        );
        return repo.save(a).getId();
    }

    public Announcement get(Long id){
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Announcement not found: " + id));
    }

    public List<Announcement> list(){
        return repo.findAll();
    }

    @Transactional
    public Announcement generateDraft(Long id){
        Announcement a = get(id);

        if (a.getStatus() != AnnouncementStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT can generate draft. current=" + a.getStatus());
        }

        Template t = templateRepo.findById(a.getTemplateId())
                .orElseThrow(() -> new NotFoundException("Template not found: " + a.getTemplateId()));

        List<String> keywords = fromJson(a.getRequiredKeywordsJson());
        GeneratedAnnouncement gen = openAiClient.generate(t.getPromptBody(), keywords, a.getTone());

        a.setDraft(gen.getTitle(), gen.getBody());
        return a;
    }

    @Transactional
    public Announcement markPendingApproval(Long id){
        Announcement a = get(id);
        if (a.getStatus() != AnnouncementStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT can submit for approval. current=" + a.getStatus());
        }
        if (a.getDraftBody() == null || a.getDraftBody().isBlank()) {
            throw new IllegalStateException("Draft is empty. Call /draft first.");
        }
        a.markPendingApproval();
        return a;
    }

    @Transactional
    public Announcement update(Long id, AnnouncementUpdateRequest req){
        Announcement a = get(id);

        if (a.getStatus() != AnnouncementStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT can be updated. current=" + a.getStatus());
        }

        if (req.getTemplateId() != null) {
            templateRepo.findById(req.getTemplateId())
                    .orElseThrow(() -> new NotFoundException("Template not found: " + req.getTemplateId()));
        }

        String keywordsJson = (req.getRequiredKeywords() == null) ? null : toJson(req.getRequiredKeywords());

        a.updateBasics(
                req.getTemplateId(),
                req.getRunAt(),
                keywordsJson,
                req.getTone(),
                req.getDiscordWebhookUrl()
        );
        return a;
    }

    @Transactional
    public Announcement submit(Long id){
        Announcement a = get(id);

        if (a.getStatus() != AnnouncementStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT can submit. current=" + a.getStatus());
        }
        if (a.getDraftBody() == null || a.getDraftBody().isBlank()) {
            throw new IllegalStateException("Draft is empty. Call /draft first.");
        }
        a.markPendingApproval();
        return a;
    }

    @Transactional
    public Announcement ensurePendingApproval(Long id){
        Announcement a = get(id);
        if (a.getStatus() == AnnouncementStatus.DRAFT) {
            return submit(id);
        }
        if (a.getStatus() != AnnouncementStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Announcement must be DRAFT or PENDING_APPROVAL. current=" + a.getStatus());
        }
        return a;
    }

    @Transactional
    public void onApprovalApproved(Long id){
        Announcement a = get(id);
        a.markApproved();
    }

    @Transactional
    public void onApprovalRejected(Long id){
        Announcement a = get(id);
        a.markDraftForRework();
    }


    private String toJson(List<String> keywords){
        try {
            if (keywords == null) return "[]";
            return objectMapper.writeValueAsString(keywords);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize keywords", e);
        }
    }

    private List<String> fromJson(String json){
        try {
            if (json == null || json.isBlank()) return List.of();
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse keywords_json", e);
        }
    }
}
