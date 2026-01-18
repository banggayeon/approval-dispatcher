package com.banggayeon.workflow_approval_dispatcher.domain.template;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "templates")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "channel_type", nullable = false, length = 20)
    private String channelType;

    @Column(name = "prompt_body", nullable = false)
    private String promptBody;

    @Column(name = "tone_presets_json")
    private String tonePresetsJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Template() {}

    public Template(String name, String channelType, String promptBody) {
        this.name = name;
        this.channelType = channelType;
        this.promptBody = promptBody;
    }

    @PrePersist
    void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getChannelType() { return channelType; }
    public String getPromptBody() { return promptBody; }
    public String getTonePresetsJson() {return tonePresetsJson; } 
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
