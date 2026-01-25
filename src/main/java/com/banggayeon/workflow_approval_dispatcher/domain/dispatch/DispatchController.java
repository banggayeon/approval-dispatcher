package com.banggayeon.workflow_approval_dispatcher.domain.dispatch;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {
    private final DispatchService service;

    public DispatchController(DispatchService service){
        this.service = service;
    }

    @PostMapping("/announcements/{announcementId}/schedule")
    public Long schedule(@PathVariable Long announcementId){
        return service.schedule(announcementId);
    }
}
