package com.banggayeon.workflow_approval_dispatcher.domain.announcement;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "announcements")
public class Announcement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="template_id", nullable=false)
    private Long templateId;

    @Column(length=200)
    private String title;

    @Column(name="draft_body", columnDefinition="text")
    private String draftBody;

    @Column(name="required_keywords_json", columnDefinition="text")
    private String requiredKeywordsJson;

    @Column(length=20)
    private String tone;

    @Column(name="run_at", nullable=false)
    private Instant runAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=255)
    private AnnouncementStatus status;

    @Column(name="discord_webhook_url", nullable=false, columnDefinition="text")
    private String discordWebhookUrl;

    @Column(name="created_by", length = 255)
    private String createdBy;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    protected Announcement() {}

    public Announcement(Long templateId, Instant runAt, String requiredKeywordsJson, 
                        String tone, String discordWebhookUrl, String createdBy){
        this.templateId = templateId;
        this.runAt = runAt;
        this.requiredKeywordsJson = requiredKeywordsJson;
        this.tone = tone;
        this.discordWebhookUrl = discordWebhookUrl;
        this.createdBy = createdBy;
        this.status = AnnouncementStatus.DRAFT;
    }

    @PrePersist
    void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = AnnouncementStatus.DRAFT;
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public void setDraft(String title, String draftBody){
        this.title = title;
        this.draftBody = draftBody;
    }

    public void markPendingApproval(){
        this.status = AnnouncementStatus.PENDING_APPROVAL;
    }

    public void markApproved(){
        this.status = AnnouncementStatus.APPROVED;
    }

    public void markScheduled(){
        this.status = AnnouncementStatus.SCHEDULED;
    }

    public void markSent(){
        this.status = AnnouncementStatus.SENT;
    }

    public void markFailed(){
        this.status = AnnouncementStatus.FAILED;
    }

    public void markDraftForRework(){
        this.status = AnnouncementStatus.DRAFT;
    }
    
    public void updateBasics(Long templateId, Instant runAt, String requiredKeywordsJson, String tone, String discordWebhookUrl){
        if (templateId != null) this.templateId = templateId;
        if (runAt != null) this.runAt = runAt;
        if (requiredKeywordsJson != null) this.requiredKeywordsJson = requiredKeywordsJson;
        if (tone != null) this.tone = tone;
        if (discordWebhookUrl != null) this.discordWebhookUrl = discordWebhookUrl;
    }
    
    public Long getId() { return id; }
    public Long getTemplateId() { return templateId; }
    public String getTitle() { return title; }
    public String getDraftBody() { return draftBody; }
    public String getRequiredKeywordsJson() { return requiredKeywordsJson; }
    public String getTone() { return tone; }
    public Instant getRunAt() { return runAt; }
    public AnnouncementStatus getStatus() { return status; }
    public String getDiscordWebhookUrl() { return discordWebhookUrl; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
