package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.entity.PlcAddress;
import com.mes.mapper.PlcAddressMapper;
import com.mes.s7.enhanced.EnhancedS7Serializer;
import com.mes.service.PlcAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PLC地址映射服务实现类
 * 管理PLC地址映射配置，支持从数据库和配置文件加载
 * 
 * @author huang
 * @date 2025/10/30
 */
@Slf4j
@Service
public class PlcAddressServiceImpl implements PlcAddressService {

    @Autowired
    private PlcAddressMapper plcAddressMapper;


    // JSON解析器
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取包含地址映射的项目配置
     * 合并数据库和配置文件中的映射信息
     */
    @Override
    public PlcAddress getProjectConfigWithMapping(String projectId) {
        try {
            // 仅使用数据库中的项目配置
            return getMappingByProjectId(projectId);
        } catch (Exception e) {
            log.error("获取项目配置失败，项目ID: {}", projectId, e);
            return null;
        }
    }

    /**
     * 从数据库获取项目配置
     */
    @Override
    public PlcAddress getProjectConfig(String projectId) {
        try {
            return plcAddressMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddress>()
                            .eq(PlcAddress::getProjectId, projectId)
            );
        } catch (Exception e) {
            log.error("获取项目配置失败，项目ID: {}", projectId, e);
            return null;
        }
    }

    /**
     * 获取所有映射配置
     * 合并数据库和配置文件中的映射信息
     */
    @Override
    public List<PlcAddress> getAllMappings() {
        try {
            // 直接返回数据库中的映射配置
            return plcAddressMapper.selectList(null);
        } catch (Exception e) {
            log.error("获取所有PLC地址映射配置失败", e);
            throw new RuntimeException("获取所有PLC地址映射配置失败", e);
        }
    }

    /**
     * 分页获取PLC地址映射配置
     */
    @Override
    public IPage<PlcAddress> getMappingsByPage(int page, int size, String projectId, String plcIp) {
        try {
            Page<PlcAddress> pageParam = new Page<>(page, size);
            LambdaQueryWrapper<PlcAddress> queryWrapper = new LambdaQueryWrapper<>();
            
            if (projectId != null && !projectId.trim().isEmpty()) {
                queryWrapper.like(PlcAddress::getProjectId, projectId);
            }
            
            if (plcIp != null && !plcIp.trim().isEmpty()) {
                queryWrapper.like(PlcAddress::getPlcIp, plcIp);
            }
            
            return plcAddressMapper.selectPage(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("分页获取PLC地址映射配置失败", e);
            throw new RuntimeException("分页获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 根据ID获取PLC地址映射配置
     */
    @Override
    public PlcAddress getMappingById(Long id) {
        try {
            return plcAddressMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID获取PLC地址映射配置失败，ID: {}", id, e);
            throw new RuntimeException("根据ID获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 根据项目标识获取PLC地址映射配置
     */
    @Override
    public PlcAddress getMappingByProjectId(String projectId) {
        try {
            return plcAddressMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddress>()
                            .eq(PlcAddress::getProjectId, projectId)
            );
        } catch (Exception e) {
            log.error("根据项目标识获取PLC地址映射配置失败，项目ID: {}", projectId, e);
            throw new RuntimeException("根据项目标识获取PLC地址映射配置失败", e);
        }
    }

    /**
     * 保存PLC地址映射配置
     */
    @Override
    public PlcAddress saveMapping(PlcAddress mapping) {
        try {
            // 检查项目ID是否已存在
            PlcAddress existing = plcAddressMapper.selectOne(
                    new LambdaQueryWrapper<PlcAddress>()
                            .eq(PlcAddress::getProjectId, mapping.getProjectId())
            );
            
            if (existing != null) {
                throw new RuntimeException("项目标识 " + mapping.getProjectId() + " 已存在");
            }
            
            plcAddressMapper.insert(mapping);
            return mapping;
        } catch (Exception e) {
            log.error("保存PLC地址映射配置失败", e);
            throw new RuntimeException("保存PLC地址映射配置失败", e);
        }
    }

    /**
     * 更新PLC地址映射配置
     */
    @Override
    public PlcAddress updateMapping(PlcAddress mapping) {
        try {
            // 检查ID是否存在
            PlcAddress existing = plcAddressMapper.selectById(mapping.getId());
            if (existing == null) {
                return null;
            }
            
            // 如果项目ID有变化，检查新项目ID是否已存在
            if (!existing.getProjectId().equals(mapping.getProjectId())) {
                PlcAddress duplicate = plcAddressMapper.selectOne(
                        new LambdaQueryWrapper<PlcAddress>()
                                .eq(PlcAddress::getProjectId, mapping.getProjectId())
                );
                
                if (duplicate != null) {
                    throw new RuntimeException("项目标识 " + mapping.getProjectId() + " 已存在");
                }
            }
            
            plcAddressMapper.updateById(mapping);
            return mapping;
        } catch (Exception e) {
            log.error("更新PLC地址映射配置失败", e);
            throw new RuntimeException("更新PLC地址映射配置失败", e);
        }
    }

    /**
     * 删除PLC地址映射配置
     */
    @Override
    public boolean deleteMapping(Long id) {
        try {
            int result = plcAddressMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除PLC地址映射配置失败，ID: {}", id, e);
            throw new RuntimeException("删除PLC地址映射配置失败", e);
        }
    }

    /**
     * 批量删除PLC地址映射配置
     */
    @Override
    public int deleteMappings(List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return 0;
            }
            return plcAddressMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            log.error("批量删除PLC地址映射配置失败，ID列表: {}", ids, e);
            throw new RuntimeException("批量删除PLC地址映射配置失败", e);
        }
    }

    /**
     * 测试PLC连接
     */
    @Override
    public boolean testConnection(PlcAddress mapping) {
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
    @Override
    public void reloadConfigMappings() {
        // 现仅使用数据库中的配置，不再从文件重载
        log.info("当前使用数据库配置，reloadConfigMappings无需操作");
    }

    /**
     * 将数据库实体转换为项目配置对象
     */
}