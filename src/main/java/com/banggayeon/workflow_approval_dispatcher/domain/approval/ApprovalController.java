package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalDecisionRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalRequestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval-requests")
public class ApprovalController {
    private final ApprovalService service;

    public ApprovalController(ApprovalService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ApprovalRequest get(@PathVariable Long id){
        return service.get(id);
    }

    @PostMapping("/{id}/approve")
    public ApprovalRequestResponse approve(@PathVariable Long id, @Valid @RequestBody ApprovalDecisionRequest req){
        return ApprovalRequestResponse.from(service.approve(id, req.getActorEmail(), req.getComment()));
    }
    
    @PostMapping("/{id}/reject")
    public ApprovalRequestResponse reject(@PathVariable Long id, @Valid @RequestBody ApprovalDecisionRequest req){
        return ApprovalRequestResponse.from(service.reject(id, req.getActorEmail(), req.getComment()));
    }
}
