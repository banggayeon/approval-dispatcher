package com.banggayeon.workflow_approval_dispatcher.domain.dispatch;

import com.banggayeon.workflow_approval_dispatcher.domain.announcement.Announcement;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.AnnouncementService;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.AnnouncementStatus;
import com.banggayeon.workflow_approval_dispatcher.domain.dispatch.client.DiscordWebhookClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DispatchService {
    private final DispatchJobRepository repo;
    private final AnnouncementService announcementService;
    private final DiscordWebhookClient discordClient;

    private final int maxAttempts;

    public DispatchService(
            DispatchJobRepository repo,
            AnnouncementService announcementService,
            DiscordWebhookClient discordClient,
            @Value("${app.dispatch.max-attempts:3}") int maxAttempts
    ) {
        this.repo = repo;
        this.announcementService = announcementService;
        this.discordClient = discordClient;
        this.maxAttempts = maxAttempts;
    }

    @Transactional
    public Long schedule(Long announcementId){
        Announcement a = announcementService.get(announcementId);

        if (a.getStatus() != AnnouncementStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED can be scheduled. current=" + a.getStatus());
        }

        DispatchJob job = new DispatchJob(announcementId, a.getRunAt());
        Long id = repo.save(job).getId();

        a.markScheduled();
        return id;
    }

    @Transactional
    public int dispatchDueNow(Instant now){
        List<DispatchJob> due = repo.findTop20ByStatusAndRunAtLessThanEqualOrderByRunAtAsc(DispatchJobStatus.READY, now);

        int sent = 0;
        for (DispatchJob job : due) {
            Announcement a = announcementService.get(job.getAnnouncementId());

            // 안전장치: 공지 상태가 SCHEDULED가 아니면 스킵(정책 선택)
            if (a.getStatus() != AnnouncementStatus.SCHEDULED) continue;

            try {
                job.markProcessing();
                job.increaseAttempt();

                discordClient.send(a.getDiscordWebhookUrl(), a.getDraftBody());

                job.markSent();
                a.markSent();
                sent++;
            } catch (Exception e) {
                String msg = e.getMessage();

                if (job.getAttemptCount() < maxAttempts) {
                    // 재시도: READY로 되돌리고 last_error만 기록 (FAILED는 최종 실패로 남김)
                    job.markFailed(msg);
                    // 정책 선택: 즉시 READY로 되돌릴지, 다음 run_at으로 미룰지
                    // 여기서는 간단히 FAILED로 두고 운영자가 확인하는 형태
                    a.markFailed();
                } else {
                    job.markFailed(msg);
                    a.markFailed();
                }
            }
        }
        return sent;
    }
}
