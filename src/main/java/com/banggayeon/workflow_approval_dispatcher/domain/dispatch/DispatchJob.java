package com.banggayeon.workflow_approval_dispatcher.domain.dispatch;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispatch_jobs")
public class DispatchJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="announcement_id", nullable = false)
    private Long announcementId;

    @Column(name="run_at", nullable = false)
    private Instant runAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DispatchJobStatus status;

    @Column(name="attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name="last_error", columnDefinition = "text")
    private String lastError;

    @Column(name="sent_at")
    private Instant sentAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    protected DispatchJob(){}

    public DispatchJob(Long announcementId, Instant runAt){
        this.announcementId = announcementId;
        this.runAt = runAt;
        this.status = DispatchJobStatus.READY;
    }

    @PrePersist
    void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = DispatchJobStatus.READY;
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public void markProcessing(){
        this.status = DispatchJobStatus.PROCESSING;
    }

    public void markSent(){
        this.status = DispatchJobStatus.SENT;
        this.sentAt = Instant.now();
        this.lastError = null;
    }

    public void markFailed(String error){
        this.status = DispatchJobStatus.FAILED;
        this.lastError = error;
    }

    public void increaseAttempt(){
        this.attemptCount++;
    }

    public Long getId() { return id; }
    public Long getAnnouncementId() { return announcementId; }
    public Instant getRunAt() { return runAt; }
    public DispatchJobStatus getStatus() { return status; }
    public int getAttemptCount() { return attemptCount; }
    public String getLastError() { return lastError; }
    public Instant getSentAt() { return sentAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
