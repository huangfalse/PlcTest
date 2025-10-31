package com.mes.controller;

import com.mes.entity.PlcBaseData;
import com.mes.service.PlcTestWriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * PLC测试写入控制器
 * 提供PLC测试写入相关的API接口
 * 
 * @author huang
 * @date 2025/10/29
 */
@Slf4j
@RestController
@RequestMapping("/api/plc/test")
@Api(tags = "PLC测试写入接口")
public class PlcTestWriteController {

    @Resource
    private PlcTestWriteService plcTestWriteService;

    @PostMapping("/simulate/request")
    @ApiOperation("模拟PLC发送请求字（触发MES任务下发）")
    public boolean simulatePlcRequest(
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则使用默认项目", example = "vertical")
            @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.trim().isEmpty()) {
            return plcTestWriteService.simulatePlcRequest(projectId.trim());
        } else {
            return plcTestWriteService.simulatePlcRequest();
        }
    }

    @PostMapping("/simulate/report")
    @ApiOperation("模拟PLC任务完成汇报")
    public boolean simulatePlcReport(
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则使用默认项目", example = "vertical")
            @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.trim().isEmpty()) {
            return plcTestWriteService.simulatePlcReport(projectId.trim());
        } else {
            return plcTestWriteService.simulatePlcReport();
        }
    }

    @PostMapping("/simulate/online")
    @ApiOperation("模拟PLC发送联机状态")
    public boolean simulateOnlineStatus(
            @ApiParam(value = "联机状态：1-联机，0-脱机", required = true, example = "1")
            @RequestParam int onlineState,
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则使用默认项目", example = "vertical")
            @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.trim().isEmpty()) {
            return plcTestWriteService.simulateOnlineStatus(onlineState, projectId.trim());
        } else {
            return plcTestWriteService.simulateOnlineStatus(onlineState);
        }
    }

    @PostMapping("/reset")
    @ApiOperation("重置PLC所有状态")
    public boolean resetPlc(
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则使用默认项目", example = "vertical")
            @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.trim().isEmpty()) {
            return plcTestWriteService.resetPlc(projectId.trim());
        } else {
            return plcTestWriteService.resetPlc();
        }
    }

    @GetMapping("/status")
    @ApiOperation("读取PLC当前状态")
    public PlcBaseData readPlcStatus(
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则使用默认项目", example = "vertical")
            @RequestParam(required = false) String projectId) {
        if (projectId != null && !projectId.trim().isEmpty()) {
            return plcTestWriteService.readPlcStatus(projectId.trim());
        } else {
            return plcTestWriteService.readPlcStatus();
        }
    }

    @PostMapping("/current-project")
    @ApiOperation("设置当前项目标识")
    public boolean setCurrentProjectId(
            @ApiParam(value = "项目标识，如vertical、horizontal等", required = true, example = "vertical")
            @RequestParam String projectId) {
        try {
            plcTestWriteService.setCurrentProjectId(projectId.trim());
            return true;
        } catch (Exception e) {
            log.error("设置当前项目标识失败", e);
            return false;
        }
    }

    @GetMapping("/current-project")
    @ApiOperation("获取当前项目标识")
    public String getCurrentProjectId() {
        return plcTestWriteService.getCurrentProjectId();
    }

    @PostMapping("/cache/clear")
    @ApiOperation("清除S7Serializer缓存")
    public boolean clearSerializerCache(
            @ApiParam(value = "项目标识，如vertical、horizontal等，不指定则清除所有缓存", example = "vertical")
            @RequestParam(required = false) String projectId) {
        try {
            if (projectId != null && !projectId.trim().isEmpty()) {
                plcTestWriteService.clearSerializerCache(projectId.trim());
            } else {
                plcTestWriteService.clearAllSerializerCache();
            }
            return true;
        } catch (Exception e) {
            log.error("清除S7Serializer缓存失败", e);
            return false;
        }
    }
}