package com.banggayeon.workflow_approval_dispatcher.domain.approval.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ApprovalRequestCreateRequest {
    private int quorum = 1;

    @NotBlank
    private String requestedBy;

    private List<String> approverEmails;

    public int getQuorum() { return quorum; }
    public String getRequestedBy() { return requestedBy; }
    public List<String> getApproverEmails() { return approverEmails; }
}
