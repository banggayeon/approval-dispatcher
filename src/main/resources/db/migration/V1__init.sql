-- V1__init.sql (PostgreSQL)

create table templates (
  id bigserial primary key,
  name varchar(80) not null,
  channel_type varchar(20) not null,
  prompt_body text not null,
  tone_presets_json text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create unique index uk_templates_name on templates(name);

-- (이 아래는 나중에 엔티티 만들 때까지는 있어도 괜찮음)
create table announcements (
  id bigserial primary key,
  template_id bigint references templates(id),
  title varchar(200) not null,
  draft_body text not null,
  required_keywords text,
  tone varchar(50),
  run_at timestamptz not null,
  status varchar(30) not null default 'DRAFT',
  created_by varchar(255),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index ix_announcements_status on announcements(status);
create index ix_announcements_run_at on announcements(run_at);
create index ix_announcements_template_id on announcements(template_id);

create table approval_requests (
  id bigserial primary key,
  announcement_id bigint not null references announcements(id),
  status varchar(30) not null default 'PENDING',
  quorum int not null default 1,
  requested_by varchar(255),
  decided_by varchar(255),
  created_at timestamptz not null default now(),
  decided_at timestamptz,
  sheet_spreadsheet_id varchar(128),
  sheet_range varchar(128),
  sheet_row_key varchar(128)
);

create index ix_approval_requests_announcement_id on approval_requests(announcement_id);
create index ix_approval_requests_status on approval_requests(status);
create index ix_approval_requests_created_at on approval_requests(created_at);

create table approval_candidates (
  approval_request_id bigint not null references approval_requests(id),
  approver_email varchar(255) not null,
  created_at timestamptz not null default now(),
  primary key (approval_request_id, approver_email)
);

create index ix_approval_candidates_approver_email on approval_candidates(approver_email);

create table approval_actions (
  id bigserial primary key,
  approval_request_id bigint not null references approval_requests(id),
  actor_email varchar(255) not null,
  action varchar(20) not null,
  comment varchar(500),
  created_at timestamptz not null default now()
);

create index ix_approval_actions_req_created on approval_actions(approval_request_id, created_at);
create index ix_approval_actions_actor_email on approval_actions(actor_email);

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

create index ix_dispatch_jobs_status_run_at on dispatch_jobs(status, run_at);
create index ix_dispatch_jobs_announcement_id on dispatch_jobs(announcement_id);
