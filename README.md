# JPA 1단계 미션 요구사항

## 엔티티 매핑
- 아래 DLL을 참고하여 객체에 엔티티를 매핑해라

### ANSWER DLL
~~~sql
create table answer
(
id          bigint generated by default as identity,
contents    clob,
created_at  timestamp not null,
deleted     boolean   not null,
question_id bigint,
updated_at  timestamp,
writer_id   bigint,
primary key (id)
)
~~~

~~~sql
create table delete_history
(
    id            bigint generated by default as identity,
    content_id    bigint,
    content_type  varchar(255),
    create_date   timestamp,
    deleted_by_id bigint,
    primary key (id)
)
~~~

~~~sql
create table question
(
    id         bigint generated by default as identity,
    contents   clob,
    created_at timestamp    not null,
    deleted    boolean      not null,
    title      varchar(100) not null,
    updated_at timestamp,
    writer_id  bigint,
    primary key (id)
)
~~~

~~~sql 
create table user
(
    id         bigint generated by default as identity,
    created_at timestamp   not null,
    email      varchar(50),
    name       varchar(20) not null,
    password   varchar(20) not null,
    updated_at timestamp,
    user_id    varchar(20) not null,
    primary key (id)
)

alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)

~~~