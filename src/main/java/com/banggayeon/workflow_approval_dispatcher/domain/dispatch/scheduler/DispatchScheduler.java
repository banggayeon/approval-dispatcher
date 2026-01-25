package com.banggayeon.workflow_approval_dispatcher.domain.dispatch.scheduler;

import com.banggayeon.workflow_approval_dispatcher.domain.dispatch.DispatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DispatchScheduler {
    private final DispatchService service;

    public DispatchScheduler(DispatchService service){
        this.service = service;
    }

    @Scheduled(fixedDelayString = "${app.dispatch.fixed-delay-ms:15000}")
    public void tick(){
        service.dispatchDueNow(Instant.now());
    }
}
