package com.banggayeon.workflow_approval_dispatcher.domain.template.dto;

import com.banggayeon.workflow_approval_dispatcher.domain.template.Template;
import java.time.Instant;

public class TemplateResponse {
    private Long id;
    private String name;
    private String channelType;
    private String body;
    private Instant createdAt;

    public static TemplateResponse from(Template t){
        TemplateResponse r = new TemplateResponse();
        r.id = t.getId();
        r.name = t.getName();
        r.channelType = t.getChannelType();
        r.body = t.getBody();
        r.createdAt = t.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getChannelType() { return channelType; }
    public String getBody() { return body; }
    public Instant getCreatedAt() { return createdAt; }
}
