create table user
(
    id            bigint unsigned auto_increment comment '主键id'
        primary key,
    username      varchar(64)                        not null comment '用户名（唯一）',
    nickname      varchar(64)                        null comment '昵称',
    email         varchar(128)                       not null comment '邮箱（唯一）',
    phone         varchar(32)                        null comment '手机号（唯一，可选）',
    password varchar(255)                       not null comment '密码哈希（bcrypt/argon2/MD5）',
    gender        tinyint  default 0                 null comment '性别：0未知 1男 2女',
    birthday      date                               null comment '生日',
    role          tinyint  default 0 comment '用户角色 0 - 普通用户 1 - 管理员',
    status        tinyint  default 1                 not null comment '状态：0禁用 1正常 2锁定',
    is_deleted    tinyint  default 0                 not null comment '逻辑删除：0正常 1已删除',
    create_time   datetime default CURRENT_TIMESTAMP not null,
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint uk_email
        unique (email),
    constraint uk_phone
        unique (phone),
    constraint uk_username
        unique (username)
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create index idx_create_time
    on user (create_time);

create index idx_status
    on user (status);