package com.banggayeon.workflow_approval_dispatcher.domain.approval.dto;

import jakarta.validation.constraints.NotBlank;

public class ApprovalDecisionRequest {
    @NotBlank
    private String actorEmail;

    private String comment;

    public String getActorEmail() { return actorEmail; }
    public String getComment() { return comment; }
}
