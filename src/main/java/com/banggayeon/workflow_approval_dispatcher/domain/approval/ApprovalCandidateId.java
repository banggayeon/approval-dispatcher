package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApprovalCandidateId implements Serializable {
    private Long approvalRequestId;
    private String approverEmail;

    protected ApprovalCandidateId() {}

    public ApprovalCandidateId(Long approvalRequestId, String approverEmail){
        this.approvalRequestId = approvalRequestId;
        this.approverEmail = approverEmail;
    }

    public Long getApprovalRequestId() { return approvalRequestId; }
    public String getApproverEmail() { return approverEmail; }    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApprovalCandidateId)) return false;
        ApprovalCandidateId that = (ApprovalCandidateId) o;
        return Objects.equals(approvalRequestId, that.approvalRequestId) &&
               Objects.equals(approverEmail, that.approverEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approvalRequestId, approverEmail);
    }
}