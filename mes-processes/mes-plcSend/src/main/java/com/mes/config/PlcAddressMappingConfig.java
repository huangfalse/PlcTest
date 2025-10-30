package com.mes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * PLC地址映射配置类
 * 支持从配置文件加载不同项目的PLC地址映射
 * 
 * @author zhoush
 * @date 2025/10/30
 */
@Data
@Component
@ConfigurationProperties(prefix = "plc.address.mapping")
public class PlcAddressMappingConfig {
    
    /**
     * 默认DB块地址
     */
    private String defaultDbArea = "DB1.";
    
    /**
     * 默认起始索引
     */
    private int defaultBeginIndex = 0;
    
    /**
     * 项目配置映射，key为项目标识，value为项目特定的PLC地址配置
     */
    private Map<String, ProjectPlcConfig> projects;
    
    /**
     * 项目PLC地址配置
     */
    @Data
    public static class ProjectPlcConfig {
        /**
         * DB块地址
         */
        private String dbArea;
        
        /**
         * 起始索引
         */
        private int beginIndex;
        
        /**
         * PLC地址映射，key为字段名，value为地址偏移量
         */
        private Map<String, Integer> addressMapping;
        
        /**
         * PLC IP地址
         */
        private String plcIp;
        
        /**
         * PLC类型
         */
        private String plcType = "S1200";
    }
    
    /**
     * 根据项目标识获取PLC配置
     * 如果项目不存在，返回默认配置
     * 
     * @param projectId 项目标识
     * @return PLC配置
     */
    public ProjectPlcConfig getProjectConfig(String projectId) {
        if (projects != null && projects.containsKey(projectId)) {
            return projects.get(projectId);
        }
        
        // 返回默认配置
        ProjectPlcConfig defaultConfig = new ProjectPlcConfig();
        defaultConfig.setDbArea(defaultDbArea);
        defaultConfig.setBeginIndex(defaultBeginIndex);
        return defaultConfig;
    }
}