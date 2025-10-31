-- 创建PLC地址映射配置表
CREATE TABLE IF NOT EXISTS `plc_address_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` varchar(50) NOT NULL COMMENT '项目标识',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `db_area` varchar(20) NOT NULL COMMENT 'DB块地址，如DB1',
  `begin_index` int(11) NOT NULL DEFAULT '0' COMMENT '起始索引',
  `plc_ip` varchar(50) DEFAULT NULL COMMENT 'PLC IP地址',
  `plc_type` varchar(20) DEFAULT 'S1200' COMMENT 'PLC类型',
  `address_mapping_json` text COMMENT '地址映射JSON配置',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：1-启用，0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_id` (`project_id`),
  KEY `idx_enabled` (`enabled`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PLC地址映射配置表';

-- 插入默认配置
INSERT INTO `plc_address_mapping` (`project_id`, `project_name`, `db_area`, `begin_index`, `plc_ip`, `plc_type`, `description`) 
VALUES ('vertical', '垂直车项目', 'DB38', 0, '192.168.10.21', 'S1200', '默认垂直车项目配置')
ON DUPLICATE KEY UPDATE `update_time` = CURRENT_TIMESTAMP;
