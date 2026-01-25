package com.banggayeon.workflow_approval_dispatcher.domain.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DispatchJobRepository extends JpaRepository<DispatchJob, Long> {
    List<DispatchJob> findTop20ByStatusAndRunAtLessThanEqualOrderByRunAtAsc(DispatchJobStatus status, Instant now);
}
