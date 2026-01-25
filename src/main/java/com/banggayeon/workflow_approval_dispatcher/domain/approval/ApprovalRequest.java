package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="announcement_id", nullable = false)
    private Long announcementId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalRequestStatus status;

    @Column(nullable = false)
    private int quorum = 1;

    @Column(name="requested_by", length = 255)
    private String requestedBy;

    @Column(name="decided_by", length = 255)
    private String decidedBy;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="decided_at")
    private Instant decidedAt;

    @Column(name="sheet_spreadsheet_id", length = 128)
    private String sheetSpreadsheetId;

    @Column(name="sheet_range", length = 128)
    private String sheetRange;

    @Column(name="sheet_row_key", length = 128)
    private String sheetRowKey;

    protected ApprovalRequest() {}

    public ApprovalRequest(Long announcementId, int quorum, String requestedBy){
        this.announcementId = announcementId;
        this.quorum = quorum <= 0 ? 1 : quorum;
        this.requestedBy = requestedBy;
        this.status = ApprovalRequestStatus.PENDING;
    }

    @PrePersist
    void onCreate(){
        this.createdAt = Instant.now();
        if (this.status == null) this.status = ApprovalRequestStatus.PENDING;
    }

    public void approve(String actorEmail){
        this.status = ApprovalRequestStatus.APPROVED;
        this.decidedBy = actorEmail;
        this.decidedAt = Instant.now();
    }

    public void reject(String actorEmail){
        this.status = ApprovalRequestStatus.REJECTED;
        this.decidedBy = actorEmail;
        this.decidedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Long getAnnouncementId() { return announcementId; }
    public ApprovalRequestStatus getStatus() { return status; }
    public int getQuorum() { return quorum; }
    public String getRequestedBy() { return requestedBy; }
    public String getDecidedBy() { return decidedBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDecidedAt() { return decidedAt; }
    public String getSheetSpreadsheetId() { return sheetSpreadsheetId; }
    public String getSheetRange() { return sheetRange; }
    public String getSheetRowKey() { return sheetRowKey; }
}
