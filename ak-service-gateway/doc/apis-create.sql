ALTER TABLE upstreams ADD COLUMN alias TEXT;

ALTER TABLE services ADD COLUMN alias TEXT;
ALTER TABLE services ADD COLUMN memo TEXT;

ALTER TABLE routes ADD COLUMN alias TEXT;
ALTER TABLE routes ADD COLUMN memo TEXT;

create table application_type (
    id                   int                  not null,
    type_name            text                 null,
    constraint PK_APPLICATION_TYPE primary key (id)
);
comment on table application_type is
'应用分类';
comment on column application_type.id is
'ID';
comment on column application_type.type_name is
'分类名称';

create table apis_classify (
    id                   uuid                 not null,
    pid                  uuid                 null,
    service_id           uuid                 null,
    alias                text                 null,
    memo                 text                 null,
    created_at           timestamptz(6)       DEFAULT timezone('UTC'::text, ('now'::text)::timestamp(3) with time zone),
    constraint PK_APIS_CLASSIFY primary key (id)
);
comment on table apis_classify is
'apis分类';
comment on column apis_classify.id is
'ID';
comment on column apis_classify.pid is
'父ID';
comment on column apis_classify.service_id is
'服务ID';
comment on column apis_classify.alias is
'别名';
comment on column apis_classify.created_at is
'创建时间';

create table swagger (
    id                   uuid                 not null,
    content              text                 null,
    constraint PK_SWAGGER primary key (id)
);
comment on table swagger is
'swagger';
comment on column swagger.id is
'ID';
comment on column swagger.content is
'文档';

-- 应用分类初始化
INSERT INTO application_type (id, type_name) VALUES ('1', '平台应用');
INSERT INTO application_type (id, type_name) VALUES ('2', '业务应用');
