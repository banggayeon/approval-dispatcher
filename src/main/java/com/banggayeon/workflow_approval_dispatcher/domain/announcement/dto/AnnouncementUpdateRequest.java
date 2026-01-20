package com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto;

import java.time.Instant;
import java.util.List;

public class AnnouncementUpdateRequest {
    private Long templateId;
    private Instant runAt;
    private List<String> requiredKeywords;
    private String tone;
    private String discordWebhookUrl;

    public Long getTemplateId() { return templateId; }
    public Instant getRunAt() { return runAt; }
    public List<String> getRequiredKeywords() { return requiredKeywords; }
    public String getTone() { return tone; }
    public String getDiscordWebhookUrl() { return discordWebhookUrl; }
}
