package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    boolean existsByAnnouncementIdAndStatus(Long announcementId, ApprovalRequestStatus status);
}
