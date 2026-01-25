package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalCandidateRepository extends JpaRepository<ApprovalCandidate, ApprovalCandidateId> {
    boolean existsByIdApprovalRequestId(Long approvalRequestId);
    boolean existsByIdApprovalRequestIdAndIdApproverEmail(Long approvalRequestId, String approverEmail);
}
