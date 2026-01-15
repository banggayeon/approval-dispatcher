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

    @Column(nullable = false, length = 20)
    private String channelType;

    @Lob
    @Column(nullable = false)
    private String body;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Template() {}

    public Template(String name, String channelType, String body) {
        this.name = name;
        this.channelType = channelType;
        this.body = body;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getChannelType() { return channelType; }
    public String getBody() { return body; }
    public Instant getCreatedAt() { return createdAt; }
}
