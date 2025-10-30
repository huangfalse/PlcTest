package com.mes.base.sysconfig.service.impl;

import cn.smallbun.screw.core.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.sysconfig.mapper.SysConfigMapper;
import com.mes.base.sysconfig.service.SysConfigService;
import com.mes.sysconfig.SysConfig;
import com.mes.sysconfig.request.SysConfigRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * (SysConfig)表服务实现类
 *
 * @author makejava
 * @since 2025-04-01 15:01:27
 */
@Service
@Slf4j
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    public Page<SysConfig> queryConfigPage(SysConfigRequest request) {
        Page<SysConfig> page = new Page<>(request.getPageNo(), request.getPageSize());
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<SysConfig>()
                .like(StringUtils.isNotBlank(request.getConfigName()), SysConfig::getConfigName, request.getConfigName())
                .like(StringUtils.isNotBlank(request.getConfigCode()), SysConfig::getConfigCode, request.getConfigCode())
                .like(StringUtils.isNotBlank(request.getConfigValue()), SysConfig::getConfigValue, request.getConfigValue())
                .eq(request.getIsEnable() != -1, SysConfig::getIsEnable, request.getIsEnable())
                .orderByAsc(SysConfig::getId);
        return this.page(page, wrapper);
    }

    @Override
    @Cacheable(value = "sysConfig", key = "#id")
    public SysConfig queryConfigById(Serializable id) {
        return this.getById(id);
    }

    @Override
    @CachePut(value = "sysConfig", key = "#request.id")
    public SysConfig saveConfig(SysConfig request) {
        this.save(request);
        return request;
    }

    @Override
    @CachePut(value = "sysConfig", key = "#request.id")
    public SysConfig updateConfig(SysConfig request) {
        this.updateById(request);
        return request;
    }

    @Override
    @CacheEvict(value = "sysConfig", key = "#id")
    public Boolean deleteConfig(Serializable id) {
        return this.removeById(id);
    }

    @Override
    public Integer queryConfigValue(Serializable id) {
        SysConfig sysConfig = this.queryConfigById(id);
        if (sysConfig == null) {
            log.info("系统配置查询为空，请检查该配置是否存在：{}", id);
            return null;
        }
        return Integer.parseInt(sysConfig.getConfigValue());
    }

    @Override
    public List<SysConfig> queryDeviceConfig(String device, Integer width) {
        QueryWrapper<SysConfig> qw = new QueryWrapper<>();
        qw.select("parent_id")
                .eq("is_enable", 1)
                .isNotNull("parent_id")
                .like("config_name", "最大长边")
                // 强制按数值比较（CAST 或 +0 都行）
                .apply("CAST(config_value AS DECIMAL(18,6)) >= {0}", width)
                .groupBy("parent_id");
        return list(qw);
    }
}

