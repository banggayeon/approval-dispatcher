package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "approval_actions")
public class ApprovalAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="approval_request_id", nullable = false)
    private Long approvalRequestId;

    @Column(name="actor_email", nullable = false, length = 255)
    private String actorEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalActionType action;

    @Column(length = 500)
    private String comment;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ApprovalAction() {}

    public ApprovalAction(Long approvalRequestId, String actorEmail, ApprovalActionType action, String comment){
        this.approvalRequestId = approvalRequestId;
        this.actorEmail = actorEmail;
        this.action = action;
        this.comment = comment;
    }

    @PrePersist
    void onCreate(){
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Long getApprovalRequestId() { return approvalRequestId; }
    public String getActorEmail() { return actorEmail; }
    public ApprovalActionType getAction() { return action; }
    public String getComment() { return comment; }
    public Instant getCreatedAt() { return createdAt; }
}
