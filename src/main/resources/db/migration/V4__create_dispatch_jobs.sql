create table dispatch_jobs (
    id bigserial primary key,

    announcement_id bigint not null references announcements(id),
    run_at timestamptz not null,

    status varchar(20) not null default 'READY',

    attempt_count int not null default 0,
    last_error text,
    sent_at timestamptz,

    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_dispatch_jobs_status_run_at on dispatch_jobs(status, run_at);
create index idx_dispatch_jobs_announcement_id on dispatch_jobs(announcement_id);
