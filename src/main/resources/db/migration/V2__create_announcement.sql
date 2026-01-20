create table announcements (
    id bigserial primary key,

    template_id bigint not null references templates(id),

    title varchar(200),
    draft_body text,

    required_keywords_json text,
    tone varchar(50),

    run_at timestamptz not null,

    status varchar(20) not null default 'DRAFT',

    discord_webhook_url text not null,

    created_by varchar(255),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_announcements_status on announcements(status);
create index idx_announcements_run_at on announcements(run_at);
create index idx_announcements_template_id on announcements(template_id);
