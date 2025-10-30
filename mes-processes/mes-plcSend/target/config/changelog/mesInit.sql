-- liquibase formatted sql

-- changeset huang:20250708121
-- preconditions onFail:CONTINUE onError:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'raw_glass_storage_station';
CREATE TABLE raw_glass_storage_station
(
    id           bigint NOT NULL,
    device_id    int DEFAULT NULL COMMENT '设备id',
    slot         int DEFAULT NULL COMMENT '格子id',
    shelf        int DEFAULT NULL COMMENT '架子号',
    enable_state int DEFAULT NULL COMMENT '启用/禁用',
    PRIMARY KEY (id)
) COMMENT '仓储工位表';
-- rollback DROP TABLE raw_glass_storage_station;

-- changeset huang:20250708122
-- preconditions onFail:CONTINUE onError:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'raw_glass_storage_details';
CREATE TABLE raw_glass_storage_details
(
    id              bigint    NOT NULL AUTO_INCREMENT,
    device_id       int                DEFAULT NULL COMMENT '设备id',
    slot            int                DEFAULT NULL COMMENT '格子id',
    raw_width       double             DEFAULT NULL COMMENT '原片宽',
    raw_height      double             DEFAULT NULL COMMENT '原片高',
    raw_thickness   double             DEFAULT NULL COMMENT '原片厚度',
    films_id        varchar(64)        DEFAULT NULL COMMENT '膜系',
    raw_margin      int                DEFAULT NULL COMMENT '原片边距',
    manufacturer    varchar(64)        DEFAULT NULL COMMENT '生厂商',
    raw_number      varchar(64)        DEFAULT NULL COMMENT '原片编号',
    start_quantity  varchar(64)        DEFAULT NULL COMMENT '开始数量', -- 新增开始数量
    remain_quantity int                DEFAULT NULL COMMENT '剩余数量',
    state           int                DEFAULT NULL COMMENT '状态',
    shelf           int                DEFAULT NULL COMMENT '架子号',
    create_time     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT '仓储详情表';
-- rollback DROP TABLE raw_glass_storage_details;

-- changeset huang:20250708123
-- preconditions onFail:CONTINUE onError:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'raw_glass_storage_task';
CREATE TABLE raw_glass_storage_history_task
(
    id                  bigint    NOT NULL AUTO_INCREMENT,
    start_slot          int                DEFAULT NULL COMMENT '起始格子',
    end_slot            int                DEFAULT NULL COMMENT '目标格子',
    raw_margin          int                DEFAULT NULL COMMENT '原片边距',
    raw_glass_width     double             DEFAULT '0' COMMENT '原片宽',
    raw_glass_height    double             DEFAULT '0' COMMENT '原片高',
    raw_glass_thickness double             DEFAULT '0' COMMENT '厚度',
    films_id            varchar(20)        DEFAULT NULL COMMENT '膜系',
    raw_quantity        int                DEFAULT NULL COMMENT '原片数量',
    task_type           int                DEFAULT NULL COMMENT '任务类型',
    task_state          int                DEFAULT NULL COMMENT '任务状态',
    shelf               int                DEFAULT NULL COMMENT '架子号',
    create_time         timestamp NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT '仓储任务表';
-- rollback DROP TABLE raw_glass_storage_task;