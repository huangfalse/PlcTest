# PLC地址映射配置功能说明

## 概述

本功能优化了PLC地址映射配置管理，支持动态配置不同项目的PLC地址映射，无需为不同项目创建不同的YAML配置文件。系统支持从数据库和配置文件两种方式加载PLC地址映射配置，优先使用数据库中的配置。

## 功能特点

1. **动态配置**：支持通过API接口动态配置不同项目的PLC地址映射
2. **多数据源**：支持从数据库和配置文件加载配置，优先使用数据库配置
3. **缓存机制**：缓存不同项目的S7Serializer实例，提高性能
4. **完整API**：提供完整的REST API接口，支持增删改查操作
5. **连接测试**：支持测试PLC连接是否正常

## 核心组件

### 1. PlcAddressMappingConfig
- 位置：`com.mes.config.PlcAddressMappingConfig`
- 功能：配置类，定义PLC地址映射配置结构
- 支持从配置文件加载默认配置

### 2. PlcAddressMapping
- 位置：`com.mes.entity.PlcAddress`
- 功能：实体类，对应数据库表结构
- 存储项目PLC地址映射配置

### 3. PlcAddressMappingMapper
- 位置：`com.mes.mapper.PlcAddressMapper`
- 功能：MyBatis Mapper接口，提供数据库操作

### 4. PlcAddressMappingService
- 位置：`com.mes.service.PlcAddressService`
- 功能：服务类，提供PLC地址映射配置的增删改查功能
- 支持从数据库和配置文件加载配置

### 5. PlcTestWriteService（已优化）
- 位置：`com.mes.service.PlcTestWriteService`
- 功能：PLC测试写入服务，支持动态配置PLC地址映射
- 缓存不同项目的S7Serializer实例

### 6. PlcAddressMappingController
- 位置：`com.mes.controller.PlcAddressController`
- 功能：控制器，提供REST API接口

## API接口

### 1. 获取所有配置
```
GET /api/plc/address-mapping/list
```

### 2. 分页获取配置
```
GET /api/plc/address-mapping/page?page=1&size=10&projectId=shuttle&plcIp=192.168.10.21
```

### 3. 根据ID获取配置
```
GET /api/plc/address-mapping/{id}
```

### 4. 根据项目ID获取配置
```
GET /api/plc/address-mapping/project/{projectId}
```

### 5. 根据项目ID获取项目配置（包含地址映射）
```
GET /api/plc/address-mapping/project/{projectId}/config
```

### 6. 创建新配置
```
POST /api/plc/address-mapping
Content-Type: application/json

{
  "projectId": "new_project",
  "projectName": "新项目",
  "dbArea": "DB1",
  "beginIndex": 0,
  "plcIp": "192.168.10.22",
  "plcType": "S1200",
  "description": "新项目配置"
}
```

### 7. 更新配置
```
PUT /api/plc/address-mapping/{id}
Content-Type: application/json

{
  "projectId": "updated_project",
  "projectName": "更新项目",
  "dbArea": "DB2",
  "beginIndex": 10,
  "plcIp": "192.168.10.23",
  "plcType": "S1500",
  "description": "更新项目配置"
}
```

### 8. 删除配置
```
DELETE /api/plc/address-mapping/{id}
```

### 9. 批量删除配置
```
DELETE /api/plc/address-mapping/batch
Content-Type: application/json

[1, 2, 3]
```

### 10. 测试PLC连接
```
POST /api/plc/address-mapping/{id}/test-connection
```

### 11. 重新加载配置文件
```
POST /api/plc/address-mapping/reload-config
```

## 配置文件示例

在`application-dev.yml`中添加以下配置：

```yaml
plc:
  address:
    mapping:
      # 默认DB块地址和起始索引
      defaultDbArea: "DB1"
      defaultBeginIndex: 0
      
      # 项目特定配置
      projects:
        shuttle:
          dbArea: "DB38"
          beginIndex: 0
          plcIp: "192.168.10.21"
          plcType: "S1200"
          addressMapping:
            onlineState: "0"
            plcRequest: "2"
            plcReport: "4"
            mesSend: "6"
            mesConfirm: "8"
            mesGlassCount: "10"
            alarmInfo: "12"
```

## 使用示例

### 1. 在代码中使用动态配置

```java
@Service
public class SomeService {
    
    @Autowired
    private PlcTestWriteService plcTestWriteService;
    
    public void doSomething() {
        // 设置当前项目
        plcTestWriteService.setCurrentProjectId("shuttle");
        
        // 使用当前项目配置执行操作
        plcTestWriteService.simulatePlcRequest();
        
        // 或者直接指定项目
        plcTestWriteService.simulatePlcRequest("another_project");
    }
}
```

### 2. 通过API管理配置

```bash
# 创建新项目配置
curl -X POST http://localhost:8080/api/plc/address-mapping \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": "new_project",
    "projectName": "新项目",
    "dbArea": "DB1",
    "beginIndex": 0,
    "plcIp": "192.168.10.22",
    "plcType": "S1200"
  }'

# 获取项目配置
curl http://localhost:8080/api/plc/address-mapping/project/new_project/config

# 测试连接
curl -X POST http://localhost:8080/api/plc/address-mapping/1/test-connection
```

## 数据库表结构

```sql
CREATE TABLE `plc_address_mapping` (
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
  UNIQUE KEY `uk_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PLC地址映射配置表';
```

## 注意事项

1. **配置优先级**：数据库中的配置优先级高于配置文件中的配置
2. **缓存管理**：当配置更新后，需要清除对应的S7Serializer缓存
3. **连接测试**：测试连接功能仅验证参数是否正确，不实际连接PLC
4. **安全性**：在生产环境中，应添加适当的权限控制和参数验证

## 扩展建议

1. **添加配置版本控制**：支持配置的版本管理和回滚
2. **添加配置变更通知**：当配置变更时，通知相关服务
3. **添加配置导入导出**：支持配置的批量导入导出
4. **添加配置模板**：支持创建配置模板，简化新项目配置
5. **添加配置验证**：增强配置参数的验证规则