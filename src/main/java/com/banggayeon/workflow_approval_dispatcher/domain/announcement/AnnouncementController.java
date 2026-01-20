package com.banggayeon.workflow_approval_dispatcher.domain.announcement;

import com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto.AnnouncementCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto.AnnouncementResponse;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto.AnnouncementUpdateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.ApprovalService;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalRequestCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalRequestResponse;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService service;
    private final ApprovalService approvalService;

    public AnnouncementController(AnnouncementService service, ApprovalService approvalService){
        this.service = service;
        this.approvalService = approvalService;
    }

    @PostMapping
    public AnnouncementResponse create(@Valid @RequestBody AnnouncementCreateRequest req){
        Long id = service.create(req);
        return AnnouncementResponse.from(service.get(id));
    }

    @GetMapping("/{id}")
    public AnnouncementResponse get(@PathVariable Long id){
        return AnnouncementResponse.from(service.get(id));
    }

    @GetMapping
    public List<AnnouncementResponse> list(){
        return service.list().stream().map(AnnouncementResponse::from).toList();
    }

    @PostMapping("/{id}/draft")
    public AnnouncementResponse draft(@PathVariable Long id){
        return AnnouncementResponse.from(service.generateDraft(id));
    }

    @PatchMapping("/{id}")
    public AnnouncementResponse update(@PathVariable Long id, @RequestBody AnnouncementUpdateRequest req){
        return AnnouncementResponse.from(service.update(id, req));
    }

    @PostMapping("/{id}/submit")
    public AnnouncementResponse submit(@PathVariable Long id){
        return AnnouncementResponse.from(service.submit(id));
    }

    @PostMapping("/{id}/request-approval")
    public ApprovalRequestResponse requestApproval(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalRequestCreateRequest req
    ){
        return approvalService.requestApprovalByAnnouncement(id, req);
    }
}
