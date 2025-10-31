package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.entity.PlcAddress;

import java.util.List;

/**
 * PLC地址映射服务接口
 * 定义PLC地址映射配置管理的核心方法
 *
 * @author huang
 * @date 2025/10/30
 */
public interface PlcAddressService {

    /**
     * 获取包含地址映射的项目配置（合并数据库与配置文件）
     * @param projectId 项目ID
     * @return 合并后的项目配置
     */
    PlcAddress getProjectConfigWithMapping(String projectId);

    /**
     * 从数据库获取项目配置
     * @param projectId 项目ID
     * @return 项目配置（PlcAddress实体），不存在则返回null
     */
    PlcAddress getProjectConfig(String projectId);

    /**
     * 获取所有映射配置（合并后的addressMapping会写回实体的JSON字段）
     * @return 所有映射配置列表
     */
    List<PlcAddress> getAllMappings();

    /**
     * 分页获取PLC地址映射配置
     * @param page 页码
     * @param size 每页大小
     * @param projectId 项目ID（可选）
     * @param plcIp PLC IP（可选）
     * @return 分页结果
     */
    IPage<PlcAddress> getMappingsByPage(int page, int size, String projectId, String plcIp);

    /**
     * 根据ID获取PLC地址映射配置
     * @param id 配置ID
     * @return PLC地址映射配置
     */
    PlcAddress getMappingById(Long id);

    /**
     * 根据项目ID获取PLC地址映射配置
     * @param projectId 项目ID
     * @return PLC地址映射配置
     */
    PlcAddress getMappingByProjectId(String projectId);

    /**
     * 创建PLC地址映射配置
     * @param mapping 映射配置
     * @return 创建后的配置
     */
    PlcAddress saveMapping(PlcAddress mapping);

    /**
     * 更新PLC地址映射配置
     * @param mapping 映射配置
     * @return 更新后的配置，未找到返回null
     */
    PlcAddress updateMapping(PlcAddress mapping);

    /**
     * 删除PLC地址映射配置
     * @param id 配置ID
     * @return 是否成功
     */
    boolean deleteMapping(Long id);

    /**
     * 批量删除PLC地址映射配置
     * @param ids 配置ID列表
     * @return 删除数量
     */
    int deleteMappings(List<Long> ids);

    /**
     * 测试PLC连接
     * @param mapping 映射配置
     * @return 是否成功
     */
    boolean testConnection(PlcAddress mapping);

    /**
     * 重新加载配置文件中的映射配置
     */
    void reloadConfigMappings();
}