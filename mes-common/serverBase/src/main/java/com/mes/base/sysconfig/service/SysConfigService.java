package com.mes.base.sysconfig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.sysconfig.SysConfig;
import com.mes.sysconfig.request.SysConfigRequest;

import java.io.Serializable;
import java.util.List;

/**
 * (SysConfig)表服务接口
 *
 * @author makejava
 * @since 2025-04-01 15:01:26
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 分页查询配置信息
     *
     * @param request
     * @return 配置信息集合
     */
    Page<SysConfig> queryConfigPage(SysConfigRequest request);

    /**
     * 按id查询配置信息
     *
     * @param id
     * @return 配置信息
     */
    SysConfig queryConfigById(Serializable id);

    /**
     * 新增配置信息
     *
     * @param request
     * @return 配置信息
     */
    SysConfig saveConfig(SysConfig request);

    /**
     * 修改配置信息
     *
     * @param request
     * @return 配置信息
     */
    SysConfig updateConfig(SysConfig request);

    /**
     * 按id删除配置信息
     *
     * @param id
     * @return 配置信息
     */
    Boolean deleteConfig(Serializable id);

    /**
     * 按id查询配置信息值
     *
     * @param id
     * @return 配置信息
     */
    Integer queryConfigValue(Serializable id);

    /**
     * 获取配置信息
     *
     * @param device 设备名
     * @param width  宽度
     * @return 配置信息
     */
    List<SysConfig> queryDeviceConfig(String device, Integer width);
}

