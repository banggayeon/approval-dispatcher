package com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto;

import com.banggayeon.workflow_approval_dispatcher.domain.announcement.Announcement;
import com.banggayeon.workflow_approval_dispatcher.domain.announcement.AnnouncementStatus;

import java.time.Instant;

public class AnnouncementResponse {
    private Long id;
    private Long templateId;
    private String title;
    private String draftBody;
    private String requiredKeywordsJson;
    private String tone;
    private Instant runAt;
    private AnnouncementStatus status;
    private String discordWebhookUrl;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    public static AnnouncementResponse from(Announcement a){
        AnnouncementResponse r = new AnnouncementResponse();
        r.id = a.getId();
        r.templateId = a.getTemplateId();
        r.title = a.getTitle();
        r.draftBody = a.getDraftBody();
        r.requiredKeywordsJson = a.getRequiredKeywordsJson();
        r.tone = a.getTone();
        r.runAt = a.getRunAt();
        r.status = a.getStatus();
        r.discordWebhookUrl = a.getDiscordWebhookUrl();
        r.createdBy = a.getCreatedBy();
        r.createdAt = a.getCreatedAt();
        r.updatedAt = a.getUpdatedAt();
        return r;
    }    

    public Long getId() { return id; }
    public Long getTemplateId() { return templateId; }
    public String getTitle() { return title; }
    public String getDraftBody() { return draftBody; }
    public String getRequiredKeywordsJson() { return requiredKeywordsJson; }
    public String getTone() { return tone; }
    public Instant getRunAt() { return runAt; }
    public AnnouncementStatus getStatus() { return status; }
    public String getDiscordWebhookUrl() { return discordWebhookUrl; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }    
}
