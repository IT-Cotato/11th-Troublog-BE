SET FOREIGN_KEY_CHECKS = 0;

create table users
(
    user_id       bigint auto_increment
        primary key,
    created_at    datetime(6)                   null,
    updated_at    datetime(6)                   null,
    bio           varchar(255)                  null,
    email         varchar(255)                  not null,
    field         varchar(255)                  null,
    github_url    varchar(255)                  null,
    is_deleted    bit                           not null,
    is_integrated bit                           not null,
    login_type    varchar(255)                  null,
    nickname      varchar(255)                  null,
    password      varchar(255)                  not null,
    profile_url   varchar(255)                  null,
    social_id     varchar(255)                  null,
    status        enum ('ACTIVE', 'INCOMPLETE') null,
    deleted_at    datetime                      null comment '삭제일시'
);

create index idx_users_deleted_at
    on users (deleted_at);

-- auto-generated definition
create table alerts
(
    alert_id   bigint auto_increment
        primary key,
    created_at datetime(6)                            null,
    updated_at datetime(6)                            null,
    alert_type enum ('COMMENTS', 'LIKES', 'TROUBLES') null,
    is_sent    bit                                    null,
    message    varchar(255)                           null,
    target_url varchar(255)                           null,
    title      varchar(255)                           null,
    user_id    bigint                                 null,
    constraint FKqx4kjyy8qmc38cpa1pj5gp74i
        foreign key (user_id) references users (user_id)
);

-- auto-generated definition
create table auth_code
(
    auth_code_id  bigint auto_increment comment '인증코드아이디'
        primary key,
    created_at    datetime(6)  null,
    updated_at    datetime(6)  null,
    auth_code     varchar(255) not null comment '인증코드',
    expire_date   datetime(6)  not null comment '만료일시',
    is_auth       bit          not null comment '인증여부',
    random_string binary(16)   not null comment '임의문자열',
    email_id      bigint       not null comment '이메일',
    constraint UKnlxi93gc1pxb38q6xiov81ep2
        unique (email_id),
    constraint FK3tncpo3mh9uqsg516lt2ywv9d
        foreign key (email_id) references email (email_id)
);

-- auto-generated definition
create table comments
(
    comment_id        bigint auto_increment
        primary key,
    content           varchar(255) not null,
    created_at        datetime(6)  not null,
    deleted_at        datetime     null,
    is_deleted        bit          not null,
    parent_comment_id bigint       null,
    post_id           bigint       not null,
    user_id           bigint       not null,
    updated_at        datetime     null comment '수정일시',
    constraint FK7h839m3lkvhbyv3bcdv7sm4fj
        foreign key (parent_comment_id) references comments (comment_id),
    constraint FK8omq0tc18jd43bu5tjh6jvraq
        foreign key (user_id) references users (user_id),
    constraint FKh4c7lvsc298whoyd4w9ta25cr
        foreign key (post_id) references posts (post_id)
);

create index idx_comments_deleted_at
    on comments (deleted_at);

-- auto-generated definition
create table contents
(
    content_id bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    body       text         null,
    sequence   int          not null,
    sub_title  varchar(255) null,
    post_id    bigint       null,
    deleted_at datetime     null comment '삭제일시',
    constraint FK4fubrl05a7neskwy9f5sl72bn
        foreign key (post_id) references posts (post_id)
);

create index idx_contents_deleted_at
    on contents (deleted_at);

create index idx_contents_post_id
    on contents (post_id);

-- auto-generated definition
create table email
(
    email_id         bigint auto_increment comment '이메일아이디'
        primary key,
    created_at       datetime(6)  null,
    updated_at       datetime(6)  null,
    email_body       text         not null comment '메일 내용',
    email_title      varchar(255) not null comment '메일 제목',
    rcvr_email_adr   varchar(255) not null comment '수신자 메일 주소',
    rcvr_name        varchar(255) null comment '수신자명',
    sender_email_adr varchar(255) not null comment '발신자 메일 주소'
);

-- auto-generated definition
create table follows
(
    follow_id    bigint auto_increment
        primary key,
    created_at   datetime(6) null,
    updated_at   datetime(6) null,
    follower_id  bigint      not null,
    following_id bigint      not null,
    constraint uk_follow_follower_following
        unique (follower_id, following_id),
    constraint FKonkdkae2ngtx70jqhsh7ol6uq
        foreign key (following_id) references users (user_id),
    constraint FKqnkw0cwwh6572nyhvdjqlr163
        foreign key (follower_id) references users (user_id)
);

create index idx_follow_follower_created
    on follows (follower_id asc, created_at desc);

create index idx_follow_following_created
    on follows (following_id asc, created_at desc);

create index idx_follow_stats_covering
    on follows (following_id, follower_id, created_at);

-- auto-generated definition
create table likes
(
    likes_id bigint auto_increment
        primary key,
    liked_at datetime(6) not null,
    post_id  bigint      not null,
    user_id  bigint      not null,
    constraint uk_likes_user_post
        unique (user_id, post_id),
    constraint FKnvx9seeqqyy71bij291pwiwrg
        foreign key (user_id) references users (user_id),
    constraint FKry8tnr4x2vwemv2bb0h5hyl0x
        foreign key (post_id) references posts (post_id)
);

-- auto-generated definition
create table post_summaries
(
    id           bigint auto_increment
        primary key,
    created_at   datetime(6)                                                         null,
    updated_at   datetime(6)                                                         null,
    summary_type enum ('INTERVIEW', 'ISSUE_MANAGEMENT', 'MEMOIRS', 'NONE', 'RESUME') null,
    post_id      bigint                                                              null,
    constraint FK73k1jt8jkldujhhie9fgfojno
        foreign key (post_id) references posts (post_id)
);

create index idx_post_summary_created
    on post_summaries (created_at);

create index idx_post_summary_post_type
    on post_summaries (post_id, summary_type);

-- auto-generated definition
create table post_tags
(
    post_tag_id           bigint auto_increment
        primary key,
    created_at            datetime(6)  null,
    updated_at            datetime(6)  null,
    post_tag_display_name varchar(100) not null,
    post_id               bigint       null,
    tag_id                bigint       null,
    deleted_at            datetime     null comment '삭제일시',
    active_yn             tinyint(1) as ((case when (`deleted_at` is null) then 1 else 0 end)) stored,
    constraint ux_post_tag_active
        unique (post_id, tag_id, active_yn),
    constraint FKkifam22p4s1nm3bkmp1igcn5w
        foreign key (post_id) references posts (post_id),
    constraint FKm6cfovkyqvu5rlm6ahdx3eavj
        foreign key (tag_id) references tags (tag_id)
);

create index idx_post_tags_post_id
    on post_tags (post_id);

create index idx_post_tags_tag_id
    on post_tags (tag_id);

-- auto-generated definition
create table posts
(
    post_id          bigint auto_increment
        primary key,
    created_at       datetime(6)                                                                       null,
    updated_at       datetime(6)                                                                       null,
    checklist_error  json                                                                              null,
    checklist_reason json                                                                              null,
    comment_count    int                                                                               not null,
    completed_at     datetime(6)                                                                       null,
    introduction     varchar(255)                                                                      null,
    is_deleted       bit                                                                               null,
    summary_created  bit                                                                               null,
    visible          bit                                                                               null,
    like_count       int                                                                               not null,
    star_rating      enum ('FIVE_STARS', 'FOUR_STARS', 'NONE', 'ONE_STAR', 'THREE_STARS', 'TWO_STARS') null,
    post_status      enum ('COMPLETED', 'SUMMARIZED', 'WRITING')                                       null,
    template_type    enum ('FREE_FORM', 'GUIDELINE')                                                   null,
    thumbnail_url    varchar(255)                                                                      null,
    title            varchar(255)                                                                      not null,
    project_id       bigint                                                                            not null,
    user_id          bigint                                                                            not null,
    deleted_at       datetime                                                                          null comment '삭제일시',
    constraint FK5lidm6cqbc7u4xhqpxm898qme
        foreign key (user_id) references users (user_id),
    constraint FKnm3g6vrsvvjgy6dyxi81m4bfg
        foreign key (project_id) references projects (project_id)
);

create index idx_posts_deleted_at
    on posts (deleted_at);

create index idx_posts_is_deleted
    on posts (is_deleted);

create index idx_posts_user_deleted_title
    on posts (user_id, is_deleted, title);

create index idx_posts_user_id
    on posts (user_id);

-- auto-generated definition
create table projects
(
    project_id          bigint auto_increment
        primary key,
    created_at          datetime(6)  null,
    updated_at          datetime(6)  null,
    deleted_at          datetime     null,
    description         varchar(255) null,
    is_deleted          bit          not null,
    name                varchar(255) not null,
    thumbnail_image_url varchar(255) null,
    user_id             bigint       not null,
    constraint FKhswfwa3ga88vxv1pmboss6jhm
        foreign key (user_id) references users (user_id)
);

create index idx_projects_deleted_at
    on projects (deleted_at);

-- auto-generated definition
create table refresh_token
(
    refresh_token_id bigint auto_increment
        primary key,
    created_at       datetime(6) null,
    updated_at       datetime(6) null,
    expired_at       datetime(6) null,
    is_revoked       bit         null,
    user_id          bigint      null,
    constraint FKjtx87i0jvq2svedphegvdwcuy
        foreign key (user_id) references users (user_id)
);

-- auto-generated definition
create table reports
(
    report_id         bigint auto_increment
        primary key,
    reporting_user_id bigint                             not null,
    reported_user_id  bigint                             not null,
    email_id          bigint                             not null,
    target_type       varchar(20)                        not null,
    target_id         bigint                             not null,
    report_type       varchar(20)                        not null,
    copyright_img_url varchar(255)                       not null,
    created_at        datetime default CURRENT_TIMESTAMP not null,
    updated_at        datetime default CURRENT_TIMESTAMP not null,
    constraint uk_reporter_target
        unique (reporting_user_id, target_type, target_id)
);

create index idx_target
    on reports (target_type, target_id);

-- auto-generated definition
create table summary_contents
(
    summary_content_id bigint auto_increment
        primary key,
    created_at         datetime(6)  null,
    updated_at         datetime(6)  null,
    body               text         not null,
    sequence           int          not null,
    sub_title          varchar(255) null,
    post_summary_id    bigint       not null,
    constraint FKf87d3icy76n18ofup2hfawosu
        foreign key (post_summary_id) references post_summaries (id)
);

-- auto-generated definition
create table tags
(
    tag_id              bigint auto_increment
        primary key,
    created_at          datetime(6)                  null,
    updated_at          datetime(6)                  null,
    tag_name            varchar(100)                 not null,
    tag_normalized_name varchar(100)                 not null,
    tag_type            enum ('ERROR', 'TECH_STACK') not null,
    tag_usage_count     int                          not null,
    constraint idx_normalized_name
        unique (tag_normalized_name)
);

create index idx_tag_type
    on tags (tag_type);

-- auto-generated definition
create table terms
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)                             null,
    updated_at        datetime(6)                             null,
    body              text                                    not null,
    deleted_at        datetime(6)                             null,
    expiration_period int                                     null,
    is_current        bit                                     not null,
    is_deleted        bit                                     null,
    is_required       bit                                     not null,
    terms_type        enum ('PRIVACY_POLICY', 'TERMS_OF_USE') not null,
    title             varchar(255)                            not null,
    version           int                                     not null,
    constraint uk_terms_type_version
        unique (terms_type, version)
);

create index idx_terms_current_only
    on terms (is_current, is_deleted);

create index idx_terms_is_deleted
    on terms (is_deleted);

create index idx_terms_type_current
    on terms (terms_type, is_current);

create index idx_terms_version
    on terms (version);

-- auto-generated definition
create table user_terms_consent
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6)                             null,
    updated_at    datetime(6)                             null,
    agreed_at     datetime(6)                             not null,
    expiration_at datetime(6)                             not null,
    is_agreed     bit                                     not null,
    is_current    bit                                     not null,
    terms_type    enum ('PRIVACY_POLICY', 'TERMS_OF_USE') not null,
    terms_id      bigint                                  not null,
    user_id       bigint                                  not null,
    constraint FKgi1ox8u2r52r13ye3yn7a50hn
        foreign key (user_id) references users (user_id),
    constraint FKgq1ej7qd0u2kyfltaqcvpvir0
        foreign key (terms_id) references terms (id)
);

create index idx_consent_user_current
    on user_terms_consent (user_id, is_current);

create index idx_consent_user_terms
    on user_terms_consent (user_id, terms_id);



SET FOREIGN_KEY_CHECKS = 1;
