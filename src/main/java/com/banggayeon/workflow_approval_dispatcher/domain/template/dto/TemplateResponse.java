package com.banggayeon.workflow_approval_dispatcher.domain.template.dto;

import com.banggayeon.workflow_approval_dispatcher.domain.template.Template;
import java.time.Instant;

public class TemplateResponse {
    private Long id;
    private String name;
    private String channelType;
    private String promptBody;
    private String tonePresetsJson;
    private Instant createdAt;
    private Instant updatedAt;
    
    public static TemplateResponse from(Template t){
        TemplateResponse r = new TemplateResponse();
        r.id = t.getId();
        r.name = t.getName();
        r.channelType = t.getChannelType();
        r.promptBody = t.getPromptBody();
        r.tonePresetsJson = t.getTonePresetsJson();
        r.createdAt = t.getCreatedAt();
        r.updatedAt = t.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getChannelType() { return channelType; }
    public String getPromptBody() { return promptBody; }
    public String getTonePresetsJson() { return tonePresetsJson; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
