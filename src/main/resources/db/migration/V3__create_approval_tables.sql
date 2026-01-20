create table approval_requests (
  id bigserial primary key,

  announcement_id bigint not null references announcements(id),

  status varchar(20) not null default 'PENDING',
  quorum int not null default 1,

  requested_by varchar(255),
  decided_by varchar(255),

  created_at timestamptz not null default now(),
  decided_at timestamptz,

  sheet_spreadsheet_id varchar(128),
  sheet_range varchar(128),
  sheet_row_key varchar(128)
);

create index idx_approval_requests_announcement_id on approval_requests(announcement_id);
create index idx_approval_requests_status on approval_requests(status);
create index idx_approval_requests_created_at on approval_requests(created_at);

create table approval_candidates (
  approval_request_id bigint not null references approval_requests(id),
  approver_email varchar(255) not null,
  created_at timestamptz not null default now(),
  primary key (approval_request_id, approver_email)
);

create index idx_approval_candidates_email on approval_candidates(approver_email);

create table approval_actions (
    id bigserial primary key,
    approval_request_id bigint not null references approval_requests(id),
    actor_email varchar(255) not null,
    action varchar(20) not null,
    comment varchar(500),
    created_at timestamptz not null default now()
);

create index idx_approval_actions_request_created_at on approval_actions(approval_request_id, created_at);
create index idx_approval_actions_actor_email on approval_actions(actor_email);
