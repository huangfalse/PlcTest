package com.mes.base.sysconfig.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.base.sysconfig.service.SysConfigService;
import com.mes.result.Result;
import com.mes.sysconfig.SysConfig;
import com.mes.sysconfig.request.SysConfigRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysConfig)表控制层
 *
 * @author makejava
 * @since 2025-04-01 15:01:15
 */
@Api(tags = "系统配置")
@RestController
@RequestMapping("/sys/sysConfig")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    @ApiOperation("分页查询所有数据")
    @PostMapping("/queryConfigPage")
    public Result<Page<SysConfig>> queryConfigPage(@RequestBody SysConfigRequest request) {
        return Result.success(sysConfigService.queryConfigPage(request));
    }

    @ApiOperation("系统配置查询通过id")
    @GetMapping("/queryConfigById")
    public Result<SysConfig> queryConfigById(Long id) {
        return Result.success(sysConfigService.queryConfigById(id));
    }

    @ApiOperation("保存系统配置")
    @PostMapping("/saveConfig")
    public Result<SysConfig> saveConfig(@RequestBody SysConfig request) {
        return Result.success(sysConfigService.saveConfig(request));
    }

    @ApiOperation("修改系统配置")
    @PostMapping("/updateConfig")
    public Result<SysConfig> updateConfig(@RequestBody SysConfig request) {
        return Result.success(sysConfigService.updateConfig(request));
    }

    @ApiOperation("删除系统配置")
    @GetMapping("/deleteConfig")
    public Result<Boolean> deleteConfig(Long id) {
        return Result.success(sysConfigService.deleteConfig(id));
    }
}

