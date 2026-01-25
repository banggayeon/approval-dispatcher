package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "approval_candidates")
public class ApprovalCandidate {
    @EmbeddedId
    private ApprovalCandidateId id;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ApprovalCandidate() {}

    public ApprovalCandidate(Long approvalRequestId, String approverEmail){
        this.id = new ApprovalCandidateId(approvalRequestId, approverEmail);
    }

    @PrePersist
    void onCreate(){
        this.createdAt = Instant.now();
    }

    public ApprovalCandidateId getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
}
