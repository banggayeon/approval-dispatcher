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
