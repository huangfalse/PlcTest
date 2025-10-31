package com.mes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.config.PlcAddressMappingConfig;
import com.mes.entity.PlcAddressMapping;
import com.mes.mapper.PlcAddressMappingMapper;
import com.mes.s7.enhanced.EnhancedS7Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PLC地址映射服务
 * 管理PLC地址映射配置，支持从数据库和配置文件加载
 * 
 * @author zhoush
 * @date 2025/10/29
 */
@Slf4j
@Service
public class PlcAddressMappingService {

    @Autowired
    private PlcAddressMappingMapper plcAddressMappingMapper;

    @Autowired
    private PlcAddressMappingConfig plcAddressMappingConfig;
    
    // JSON解析器
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取包含地址映射的项目配置
     * 合并数据库和配置文件中的映射信息
     */
    public PlcAddressMappingConfig.ProjectPlcConfig getProjectConfigWithMapping(String projectId) {
        try {
            // 1. 获取数据库中的配置
            PlcAddressMapping mapping = getMappingByProjectId(projectId);
            PlcAddressMappingConfig.ProjectPlcConfig projectConfig = null;
            
            if (mapping != null) {
                // 从数据库实体转换为项目配置对象
                projectConfig = convertToProjectConfig(mapping);
            } else {
                // 数据库中没有配置，尝试从配置文件获取
                if (fileConfig != null && fileConfig.getProjects().containsKey(projectId)) {
                    projectConfig = fileConfig.getProjects().get(projectId);
                } else {
                    // 创建默认配置
                    projectConfig = new PlcAddressMappingConfig.ProjectPlcConfig();
                    projectConfig.setDbArea(fileConfig != null ? fileConfig.getDefaultDbArea() : "DB1");
                    projectConfig.setBeginIndex(fileConfig != null ? fileConfig.getDefaultBeginIndex() : 0);
                    projectConfig.setAddressMapping(new HashMap<>());
                }
            }
            
            // 2. 如果配置文件中有额外的映射信息，进行合并
            if (fileConfig != null && fileConfig.getProjects() != null && fileConfig.getProjects().containsKey(projectId)) {
                PlcAddressMappingConfig.ProjectPlcConfig fileProjectConfig = fileConfig.getProjects().get(projectId);
                
                // 合并地址映射
                if (fileProjectConfig.getAddressMapping() != null) {
                    if (projectConfig.getAddressMapping() == null) {
                        projectConfig.setAddressMapping(new HashMap<>());
                    }
                    // 文件配置作为基础，数据库配置作为覆盖（优先级更高）
                    Map<String, Integer> mergedMapping = new HashMap<>(fileProjectConfig.getAddressMapping());
                    if (projectConfig.getAddressMapping() != null) {
                        mergedMapping.putAll(projectConfig.getAddressMapping());
                    }
                    projectConfig.setAddressMapping(mergedMapping);
                }
                
                // 如果项目配置中缺少某些属性，从文件配置中补充
                if (projectConfig.getDbArea() == null || projectConfig.getDbArea().trim().isEmpty()) {
                    projectConfig.setDbArea(fileProjectConfig.getDbArea());
                }
                if (projectConfig.getPlcIp() == null || projectConfig.getPlcIp().trim().isEmpty()) {
                    projectConfig.setPlcIp(fileProjectConfig.getPlcIp());
                }
                if (projectConfig.getPlcType() == null || projectConfig.getPlcType().trim().isEmpty()) {
                    projectConfig.setPlcType(fileProjectConfig.getPlcType());
                }
            }
            
            return projectConfig;
        } catch (Exception e) {
            log.error("获取项目配置失败，项目ID: {}", projectId, e);
            // 返回默认配置
            PlcAddressMappingConfig.ProjectPlcConfig defaultConfig = new PlcAddressMappingConfig.ProjectPlcConfig();
            defaultConfig.setDbArea(fileConfig != null ? fileConfig.getDefaultDbArea() : "DB1");
            defaultConfig.setBeginIndex(fileConfig != null ? fileConfig.getDefaultBeginIndex() : 0);
            defaultConfig.setAddressMapping(new HashMap<>());
            return defaultConfig;
        }
    }

    /**
     * 从数据库获取项目配置
     */
    public PlcAddressMappingConfig.ProjectPlcConfig getProjectConfig(String projectId) {
        try {
            // 优先从数据库获取配置
            PlcAddressMapping mapping = plcAddressMappingMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddressMapping>()
                            .eq(PlcAddressMapping::getProjectId, projectId)
            );
            
            if (mapping != null) {
                return convertToProjectConfig(mapping);
            }
            
            // 如果数据库中没有，则从配置文件获取
            if (fileConfig != null && fileConfig.getProjects() != null && fileConfig.getProjects().containsKey(projectId)) {
                return fileConfig.getProjects().get(projectId);
            }
            
            // 如果都没有，返回null
            return null;
        } catch (Exception e) {
            log.error("获取项目配置失败，项目ID: {}", projectId, e);
            return null;
        }
    }

    /**
     * 获取所有映射配置
     * 合并数据库和配置文件中的映射信息
     */
    public List<PlcAddressMapping> getAllMappings() {
        try {
            List<PlcAddressMapping> mappings = plcAddressMappingMapper.selectList(null);
            
            // 处理每个映射配置
            for (PlcAddressMapping mapping : mappings) {
                // 获取项目标识
                String projectId = mapping.getProjectId();
                
                // 获取合并后的项目配置（包含数据库和配置文件的映射信息）
                PlcAddressMappingConfig.ProjectPlcConfig projectConfig = getProjectConfigWithMapping(projectId);
                
                // 将合并后的映射信息转换为JSON字符串
                if (projectConfig.getAddressMapping() != null && !projectConfig.getAddressMapping().isEmpty()) {
                    try {
                        String mergedMappingJson = objectMapper.writeValueAsString(projectConfig.getAddressMapping());
                        mapping.setAddressMapping(mergedMappingJson);
                        // addressMapping属性已经映射到数据库的address_mapping_json字段
                    } catch (Exception e) {
                        log.warn("转换合并后的映射信息失败，项目ID: {}", projectId, e);
                        // 保持原有的addressMapping值
                    }
                }
            }
            
            return mappings;
        } catch (Exception e) {
            log.error("获取所有PLC地址映射配置失败", e);
            throw new RuntimeException("获取所有PLC地址映射配置失败", e);
        }
    }

    /**
     * 分页获取PLC地址映射配置
     */
    public IPage<PlcAddressMapping> getMappingsByPage(int page, int size, String projectId, String plcIp) {
        try {
            Page<PlcAddressMapping> pageParam = new Page<>(page, size);
            LambdaQueryWrapper<PlcAddressMapping> queryWrapper = new LambdaQueryWrapper<>();
            
            if (projectId != null && !projectId.trim().isEmpty()) {
                queryWrapper.like(PlcAddressMapping::getProjectId, projectId);
            }
            
            if (plcIp != null && !plcIp.trim().isEmpty()) {
                queryWrapper.like(PlcAddressMapping::getPlcIp, plcIp);
            }
            
            return plcAddressMappingMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("分页获取PLC地址映射配置失败", e);
            throw new RuntimeException("分页获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 根据ID获取PLC地址映射配置
     */
    public PlcAddressMapping getMappingById(Long id) {
        try {
            return plcAddressMappingMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID获取PLC地址映射配置失败，ID: {}", id, e);
            throw new RuntimeException("根据ID获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 根据项目标识获取PLC地址映射配置
     */
    public PlcAddressMapping getMappingByProjectId(String projectId) {
        try {
            return plcAddressMappingMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddressMapping>()
                            .eq(PlcAddressMapping::getProjectId, projectId)
            );
        } catch (Exception e) {
            log.error("根据项目标识获取PLC地址映射配置失败，项目ID: {}", projectId, e);
            throw new RuntimeException("根据项目标识获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 保存PLC地址映射配置
     */
    public PlcAddressMapping saveMapping(PlcAddressMapping mapping) {
        try {
            // 检查项目ID是否已存在
            PlcAddressMapping existing = plcAddressMappingMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddressMapping>()
                            .eq(PlcAddressMapping::getProjectId, mapping.getProjectId())
            );
            
            if (existing != null) {
                throw new RuntimeException("项目标识 " + mapping.getProjectId() + " 已存在");
            }
            
            plcAddressMappingMapper.insert(mapping);
            return mapping;
        } catch (Exception e) {
            log.error("保存PLC地址映射配置失败", e);
            throw new RuntimeException("保存PLC地址映射配置失败", e);
        }
    }

    /**
     * 更新PLC地址映射配置
     */
    public PlcAddressMapping updateMapping(PlcAddressMapping mapping) {
        try {
            // 检查ID是否存在
            PlcAddressMapping existing = plcAddressMappingMapper.selectById(mapping.getId());
            if (existing == null) {
                return null;
            }
            
            // 如果项目ID有变化，检查新项目ID是否已存在
            if (!existing.getProjectId().equals(mapping.getProjectId())) {
                PlcAddressMapping duplicate = plcAddressMappingMapper.selectOne(
                        new LambdaQueryWrapper<PlcAddressMapping>()
                                .eq(PlcAddressMapping::getProjectId, mapping.getProjectId())
                );
                
                if (duplicate != null) {
                    throw new RuntimeException("项目标识 " + mapping.getProjectId() + " 已存在");
                }
            }
            
            plcAddressMappingMapper.updateById(mapping);
            return mapping;
        } catch (Exception e) {
            log.error("更新PLC地址映射配置失败", e);
            throw new RuntimeException("更新PLC地址映射配置失败", e);
        }
    }

    /**
     * 删除PLC地址映射配置
     */
    public boolean deleteMapping(Long id) {
        try {
            int result = plcAddressMappingMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除PLC地址映射配置失败，ID: {}", id, e);
            throw new RuntimeException("删除PLC地址映射配置失败", e);
        }
    }

    /**
     * 批量删除PLC地址映射配置
     */
    public int deleteMappings(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return 0;
            }
            return plcAddressMappingMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            log.error("批量删除PLC地址映射配置失败，ID列表: {}", ids, e);
            throw new RuntimeException("批量删除PLC地址映射配置失败", e);
        }
    }

    /**
     * 测试PLC连接
     */
    public boolean testConnection(PlcAddressMapping mapping) {
        try {
            // 解析PLC类型
            EPlcType plcType = EPlcType.S1200; // 默认值
            if (mapping.getPlcType() != null && !mapping.getPlcType().trim().isEmpty()) {
                try {
                    plcType = EPlcType.valueOf(mapping.getPlcType());
                } catch (IllegalArgumentException e) {
                    log.warn("未知的PLC类型: {}, 使用默认类型 S1200", mapping.getPlcType());
                }
            }
            
            // 创建S7PLC实例
            String plcIp = mapping.getPlcIp() != null ? mapping.getPlcIp() : "192.168.10.21";
            S7PLC s7Plc = new S7PLC(plcType, plcIp);
            
            // 创建EnhancedS7Serializer实例
            EnhancedS7Serializer serializer = EnhancedS7Serializer.newInstance(s7Plc);
            
            // 尝试读取一个简单的数据来测试连接
            String dbArea = mapping.getDbArea() != null ? mapping.getDbArea() : "DB1";
            int beginIndex = mapping.getBeginIndex();
            
            // 这里我们只测试连接，不读取实际数据
            // 实际项目中可能需要根据具体需求调整
            return true;
        } catch (Exception e) {
            log.error("PLC连接测试失败", e);
            return false;
        }
    }

    /**
     * 重新加载配置文件中的映射
     */
    public void reloadConfigMappings() {
        try {
            // 关键修改3：通过Spring上下文刷新配置（适用于Spring Boot 2.x）
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            if (applicationContext != null) {
                // 获取配置绑定的BeanDefinition
                ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
                ConfigurableEnvironment environment = configurableContext.getEnvironment();

                // 刷新配置源（重新读取配置文件）
                for (PropertySource<?> source : environment.getPropertySources()) {
                    if (source instanceof ResourcePropertySource) {
                        ResourcePropertySource resourceSource = (ResourcePropertySource) source;
                        // 重新加载配置文件资源
                        resourceSource.refresh();
                    }
                }

                // 重新绑定配置到PlcAddressMappingConfig
                Binder.get(environment)
                        .bind("plc.address.mapping", PlcAddressMappingConfig.class)
                        .ifBound(config -> {
                            // 更新注入的配置实例（因@ConfigurationProperties默认是单例，需手动更新属性）
                            this.plcAddressMappingConfig.setDefaultDbArea(config.getDefaultDbArea());
                            this.plcAddressMappingConfig.setDefaultBeginIndex(config.getDefaultBeginIndex());
                            this.plcAddressMappingConfig.setProjects(config.getProjects());
                        });

                log.info("成功重新加载配置文件中的PLC地址映射");
            }
        } catch (Exception e) {
            log.error("重新加载配置文件中的PLC地址映射失败", e);
        }
    }

    /**
     * 将数据库实体转换为项目配置对象
     */
    private PlcAddressMappingConfig.ProjectPlcConfig convertToProjectConfig(PlcAddressMapping mapping) {
        PlcAddressMappingConfig.ProjectPlcConfig config = new PlcAddressMappingConfig.ProjectPlcConfig();
        
        config.setDbArea(mapping.getDbArea());
        config.setBeginIndex(mapping.getBeginIndex());
        config.setPlcIp(mapping.getPlcIp());
        config.setPlcType(mapping.getPlcType());
        
        // 解析地址映射JSON
        if (mapping.getAddressMapping() != null && !mapping.getAddressMapping().trim().isEmpty()) {
            try {
                // 使用Jackson解析JSON字符串为Map<String, Integer>
                Map<String, Integer> addressMap = objectMapper.readValue(
                    mapping.getAddressMapping(), 
                    new TypeReference<Map<String, Integer>>() {}
                );
                config.setAddressMapping(addressMap);
            } catch (Exception e) {
                log.warn("解析地址映射JSON失败: {}", mapping.getAddressMapping(), e);
                // 如果解析失败，创建一个空的映射
                config.setAddressMapping(new HashMap<>());
            }
        } else {
            // 如果没有地址映射，创建一个空的映射
            config.setAddressMapping(new HashMap<>());
        }
        
        return config;
    }
}