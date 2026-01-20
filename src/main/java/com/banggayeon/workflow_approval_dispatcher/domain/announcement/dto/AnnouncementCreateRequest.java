package com.banggayeon.workflow_approval_dispatcher.domain.announcement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public class AnnouncementCreateRequest {
    @NotNull
    private Long templateId;

    @NotNull
    private Instant runAt;

    private List<String> requiredKeywords;
    private String tone;

    @NotBlank
    private String discordWebhookUrl;

    private String createdBy;

    public Long getTemplateId() { return templateId; }
    public Instant getRunAt() { return runAt; }
    public List<String> getRequiredKeywords() { return requiredKeywords; }
    public String getTone() { return tone; }
    public String getDiscordWebhookUrl() { return discordWebhookUrl; }
    public String getCreatedBy() { return createdBy; }
}
