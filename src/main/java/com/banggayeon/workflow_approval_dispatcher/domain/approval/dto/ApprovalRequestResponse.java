package com.banggayeon.workflow_approval_dispatcher.domain.approval.dto;

import com.banggayeon.workflow_approval_dispatcher.domain.approval.ApprovalRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.ApprovalRequestStatus;

import java.time.Instant;

public class ApprovalRequestResponse {
    private Long id;
    private Long announcementId;
    private ApprovalRequestStatus status;
    private int quorum;
    private String requestedBy;
    private String decidedBy;
    private Instant createdAt;
    private Instant decidedAt;

    public static ApprovalRequestResponse from(ApprovalRequest ar){
        ApprovalRequestResponse r = new ApprovalRequestResponse();
        r.id = ar.getId();
        r.announcementId = ar.getAnnouncementId();
        r.status = ar.getStatus();
        r.quorum = ar.getQuorum();
        r.requestedBy = ar.getRequestedBy();
        r.decidedBy = ar.getDecidedBy();
        r.createdAt = ar.getCreatedAt();
        r.decidedAt = ar.getDecidedAt();
        return r;
    }

    public Long getId() { return id; }
    public Long getAnnouncementId() { return announcementId; }
    public ApprovalRequestStatus getStatus() { return status; }
    public int getQuorum() { return quorum; }
    public String getRequestedBy() { return requestedBy; }
    public String getDecidedBy() { return decidedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDecidedAt() { return decidedAt; }
}
