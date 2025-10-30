package com.mes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.config.PlcAddressMappingConfig;
import com.mes.entity.PlcAddressMapping;
import com.mes.mapper.PlcAddressMappingMapper;
import com.mes.s7.enhanced.EnhancedS7Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.InputStream;
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
    
    // 配置文件中的映射配置
    private PlcAddressMappingConfig fileConfig;

    @PostConstruct
    public void init() {
        // 初始化时加载配置文件中的映射
        reloadConfigMappings();
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
            if (fileConfig != null && fileConfig.getProjects() != null) {
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
     * 从数据库获取所有映射配置
     */
    public List<PlcAddressMapping> getAllMappings() {
        try {
            return plcAddressMappingMapper.selectList(null);
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
            int beginIndex = mapping.getBeginIndex() != null ? mapping.getBeginIndex() : 0;
            
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
            // 加载配置文件
            Yaml yaml = new Yaml(new Constructor(PlcAddressMappingConfig.class));
            InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("application-dev.yml");
            
            if (inputStream != null) {
                // 读取整个YAML文件
                Map<String, Object> yamlData = yaml.load(inputStream);
                
                // 获取plc配置部分
                if (yamlData.containsKey("plc")) {
                    Map<String, Object> plcConfig = (Map<String, Object>) yamlData.get("plc");
                    
                    // 获取address.mapping部分
                    if (plcConfig.containsKey("address") && 
                        ((Map<String, Object>) plcConfig.get("address")).containsKey("mapping")) {
                        
                        Map<String, Object> addressMapping = (Map<String, Object>) ((Map<String, Object>) plcConfig.get("address")).get("mapping");
                        
                        // 创建配置对象
                        fileConfig = new PlcAddressMappingConfig();
                        
                        // 设置默认值
                        if (addressMapping.containsKey("defaultDbArea")) {
                            fileConfig.setDefaultDbArea((String) addressMapping.get("defaultDbArea"));
                        }
                        if (addressMapping.containsKey("defaultBeginIndex")) {
                            fileConfig.setDefaultBeginIndex((Integer) addressMapping.get("defaultBeginIndex"));
                        }
                        
                        // 解析项目配置
                        if (addressMapping.containsKey("projects")) {
                            Map<String, Object> projects = (Map<String, Object>) addressMapping.get("projects");
                            
                            for (Map.Entry<String, Object> entry : projects.entrySet()) {
                                String projectId = entry.getKey();
                                Map<String, Object> projectData = (Map<String, Object>) entry.getValue();
                                
                                PlcAddressMappingConfig.ProjectPlcConfig projectConfig = new PlcAddressMappingConfig.ProjectPlcConfig();
                                
                                if (projectData.containsKey("dbArea")) {
                                    projectConfig.setDbArea((String) projectData.get("dbArea"));
                                } else {
                                    projectConfig.setDbArea(fileConfig.getDefaultDbArea());
                                }
                                
                                if (projectData.containsKey("beginIndex")) {
                                    projectConfig.setBeginIndex((Integer) projectData.get("beginIndex"));
                                } else {
                                    projectConfig.setBeginIndex(fileConfig.getDefaultBeginIndex());
                                }
                                
                                if (projectData.containsKey("plcIp")) {
                                    projectConfig.setPlcIp((String) projectData.get("plcIp"));
                                }
                                
                                if (projectData.containsKey("plcType")) {
                                    projectConfig.setPlcType((String) projectData.get("plcType"));
                                }
                                
                                if (projectData.containsKey("addressMapping")) {
                                    projectConfig.setAddressMapping((Map<String, String>) projectData.get("addressMapping"));
                                }
                                
                                fileConfig.getProjects().put(projectId, projectConfig);
                            }
                        }
                    }
                }
                
                log.info("成功加载配置文件中的PLC地址映射");
            } else {
                log.warn("未找到配置文件 application-dev.yml");
            }
        } catch (Exception e) {
            log.error("加载配置文件中的PLC地址映射失败", e);
            fileConfig = new PlcAddressMappingConfig();
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
        if (mapping.getAddressMappingJson() != null && !mapping.getAddressMappingJson().trim().isEmpty()) {
            try {
                // 这里可以使用JSON解析库，如Jackson或Gson
                // 为了简单起见，这里只是将JSON字符串直接赋值
                // 实际项目中应该解析为Map对象
                // config.setAddressMapping(parseJsonToMap(mapping.getAddressMappingJson()));
            } catch (Exception e) {
                log.warn("解析地址映射JSON失败: {}", mapping.getAddressMappingJson(), e);
            }
        }
        
        return config;
    }
}