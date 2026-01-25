package com.banggayeon.workflow_approval_dispatcher.domain.approval;

import com.banggayeon.workflow_approval_dispatcher.domain.announcement.Announcement;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.AnnouncementService;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.AnnouncementStatus;

import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalRequestCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.approval.dto.ApprovalRequestResponse;

import com.banggayeon.workflow_approval_dispatcher.domain.dispatch.DispatchService;
import com.banggayeon.workflow_approval_dispatcher.global.error.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ApprovalService {
    private final ApprovalRequestRepository requestRepo;
    private final ApprovalCandidateRepository candidateRepo;
    private final ApprovalActionRepository actionRepo;
    private final AnnouncementService announcementService;
    private final DispatchService dispatchService;

    public ApprovalService(
            ApprovalRequestRepository requestRepo,
            ApprovalCandidateRepository candidateRepo,
            ApprovalActionRepository actionRepo,
            AnnouncementService announcementService,
            DispatchService dispatchService
    ) {
        this.requestRepo = requestRepo;
        this.candidateRepo = candidateRepo;
        this.actionRepo = actionRepo;
        this.announcementService = announcementService;
        this.dispatchService = dispatchService;
    }

    @Transactional
    public Long requestApproval(Long announcementId, ApprovalRequestCreateRequest req){
        Announcement a = announcementService.get(announcementId);

        if (a.getStatus() != AnnouncementStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Announcement must be PENDING_APPROVAL before creating approval_request. current=" + a.getStatus());
        }

        ApprovalRequest ar = new ApprovalRequest(announcementId, req.getQuorum(), req.getRequestedBy());
        Long id = requestRepo.save(ar).getId();

        List<String> candidates = req.getApproverEmails();
        if (candidates != null) {
            for (String email : candidates) {
                if (email == null || email.isBlank()) continue;
                candidateRepo.save(new ApprovalCandidate(id, email));
            }
        }

        actionRepo.save(new ApprovalAction(id, req.getRequestedBy(), ApprovalActionType.ATTEMPT, "Requested approval"));
        return id;
    }

    @Transactional
    public ApprovalRequestResponse requestApprovalByAnnouncement(Long announcementId, ApprovalRequestCreateRequest req){
        // DRAFT면 submit까지 포함해서 PENDING_APPROVAL로 올려줌
        announcementService.ensurePendingApproval(announcementId);

        // 같은 공지에 PENDING 요청이 이미 있으면 막기
        if (requestRepo.existsByAnnouncementIdAndStatus(announcementId, ApprovalRequestStatus.PENDING)) {
            throw new IllegalStateException("Already has PENDING approval_request for announcement: " + announcementId);
        }

        ApprovalRequest ar = new ApprovalRequest(announcementId, req.getQuorum(), req.getRequestedBy());
        Long id = requestRepo.save(ar).getId();

        List<String> candidates = req.getApproverEmails();
        if (candidates != null) {
            for (String email : candidates) {
                if (email == null || email.isBlank()) continue;
                candidateRepo.save(new ApprovalCandidate(id, email));
            }
        }

        actionRepo.save(new ApprovalAction(id, req.getRequestedBy(), ApprovalActionType.ATTEMPT, "Requested approval"));
        return ApprovalRequestResponse.from(get(id));
    }

    public ApprovalRequest get(Long id){
        return requestRepo.findById(id).orElseThrow(() -> new NotFoundException("ApprovalRequest not found: " + id));
    }

    @Transactional
    public ApprovalRequest approve(Long id, String actorEmail, String comment){
        ApprovalRequest ar = get(id);
        if (ar.getStatus() != ApprovalRequestStatus.PENDING) {
            throw new IllegalStateException("ApprovalRequest is not PENDING. current=" + ar.getStatus());
        }

        // 후보가 등록돼 있으면 후보인지 체크
        if (candidateRepo.existsByIdApprovalRequestId(id)) {
            boolean ok = candidateRepo.existsByIdApprovalRequestIdAndIdApproverEmail(id, actorEmail);
            if (!ok) throw new IllegalStateException("Actor is not in approval candidates: " + actorEmail);
        }

        ar.approve(actorEmail);
        actionRepo.save(new ApprovalAction(id, actorEmail, ApprovalActionType.APPROVE, comment));

        // 공지 승인 처리 + 바로 스케줄 등록(= dispatch_jobs 생성)
        announcementService.onApprovalApproved(ar.getAnnouncementId());
        dispatchService.schedule(ar.getAnnouncementId()); // 여기서 announcement.status = SCHEDULED

        return ar;
    }

    @Transactional
    public ApprovalRequest reject(Long id, String actorEmail, String comment){
        ApprovalRequest ar = get(id);
        if (ar.getStatus() != ApprovalRequestStatus.PENDING) {
            throw new IllegalStateException("ApprovalRequest is not PENDING. current=" + ar.getStatus());
        }

        if (candidateRepo.existsByIdApprovalRequestId(id)) {
            boolean ok = candidateRepo.existsByIdApprovalRequestIdAndIdApproverEmail(id, actorEmail);
            if (!ok) throw new IllegalStateException("Actor is not in approval candidates: " + actorEmail);
        }

        ar.reject(actorEmail);
        actionRepo.save(new ApprovalAction(id, actorEmail, ApprovalActionType.REJECT, comment));

        // ✅ 거절되면 DRAFT로 되돌려서 수정 후 재요청 가능하게
        announcementService.onApprovalRejected(ar.getAnnouncementId());

        return ar;
    }
}
