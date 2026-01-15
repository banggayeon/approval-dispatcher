package com.banggayeon.workflow_approval_dispatcher.domain.template.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TemplateCreateRequest {
    @NotBlank
    @Size(max = 80)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String channelType;

    @NotBlank
    private String body;

    public String getName() { return name; }
    public String getChannelType() { return channelType; }
    public String getBody() { return body; }
}   