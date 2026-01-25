package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalActionRepository extends JpaRepository<ApprovalAction, Long> {
}
