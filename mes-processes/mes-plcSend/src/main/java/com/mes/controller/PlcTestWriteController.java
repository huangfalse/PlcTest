package com.mes.controller;

import com.mes.entity.PlcBaseData;
import com.mes.service.PlcAutoTestService;
import com.mes.service.PlcTestWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * PLC测试写入控制器
 * 为前端提供模拟PLC写入操作的接口
 * 
 * @author zhoush
 * @date 2025/10/29
 */
@Slf4j
@RestController
@RequestMapping("/plcTest/write")
public class PlcTestWriteController {

    @Resource
    private PlcTestWriteService plcTestWriteService;
    
    @Resource
    private PlcAutoTestService plcAutoTestService;

    /**
     * 模拟PLC发送请求字
     */
    @PostMapping("/request")
    public Map<String, Object> simulateRequest(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success;
            if (params != null && params.containsKey("dbBlock")) {
                String dbBlock = (String) params.get("dbBlock");
                int beginIndex = (int) params.getOrDefault("beginIndex", 0);
                success = plcTestWriteService.simulatePlcRequest(dbBlock, beginIndex);
            } else {
                success = plcTestWriteService.simulatePlcRequest();
            }
            
            result.put("success", success);
            result.put("message", success ? "PLC请求字发送成功" : "发送失败");
            
            // 返回当前状态
            PlcBaseData status = plcTestWriteService.readPlcStatus();
            result.put("currentStatus", status);
            
        } catch (Exception e) {
            log.error("模拟PLC请求异常", e);
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 模拟PLC任务完成汇报
     */
    @PostMapping("/report")
    public Map<String, Object> simulateReport(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success;
            if (params != null && params.containsKey("dbBlock")) {
                String dbBlock = (String) params.get("dbBlock");
                int beginIndex = (int) params.getOrDefault("beginIndex", 0);
                success = plcTestWriteService.simulatePlcReport(dbBlock, beginIndex);
            } else {
                success = plcTestWriteService.simulatePlcReport();
            }
            
            result.put("success", success);
            result.put("message", success ? "PLC任务汇报成功" : "汇报失败");
            
            PlcBaseData status = plcTestWriteService.readPlcStatus();
            result.put("currentStatus", status);
            
        } catch (Exception e) {
            log.error("模拟上片PLC汇报异常", e);
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 模拟PLC联机状态
     */
    @PostMapping("/online")
    public Map<String, Object> simulateOnline(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int onlineState = (int) params.getOrDefault("onlineState", 1);
            boolean success;
            
            if (params.containsKey("dbBlock")) {
                String dbBlock = (String) params.get("dbBlock");
                int beginIndex = (int) params.getOrDefault("beginIndex", 0);
                success = plcTestWriteService.simulateOnlineStatus(onlineState, dbBlock, beginIndex);
            } else {
                success = plcTestWriteService.simulateOnlineStatus(onlineState);
            }
            
            result.put("success", success);
            result.put("message", success ? "PLC联机状态设置成功: " + onlineState : "设置失败");
            
            PlcBaseData status = plcTestWriteService.readPlcStatus();
            result.put("currentStatus", status);
            
        } catch (Exception e) {
            log.error("模拟PLC联机状态异常", e);
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 重置PLC状态
     */
    @PostMapping("/reset")
    public Map<String, Object> resetPlc(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success;
            if (params != null && params.containsKey("dbBlock")) {
                String dbBlock = (String) params.get("dbBlock");
                int beginIndex = (int) params.getOrDefault("beginIndex", 0);
                success = plcTestWriteService.resetPlc(dbBlock, beginIndex);
            } else {
                success = plcTestWriteService.resetPlc();
            }
            
            result.put("success", success);
            result.put("message", success ? "PLC状态重置成功" : "重置失败");
            
            PlcBaseData status = plcTestWriteService.readPlcStatus();
            result.put("currentStatus", status);
            
        } catch (Exception e) {
            log.error("重置上片PLC状态异常", e);
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 读取PLC当前状态
     */
    @GetMapping("/status")
    public Map<String, Object> getPlcStatus(@RequestParam(required = false) String dbBlock, 
                                           @RequestParam(required = false, defaultValue = "0") Integer beginIndex) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            PlcBaseData status;
            if (dbBlock != null) {
                status = plcTestWriteService.readPlcStatus(dbBlock, beginIndex);
            } else {
                status = plcTestWriteService.readPlcStatus();
            }
            
            result.put("success", true);
            result.put("data", status);
            result.put("message", "获取上片PLC状态成功");
            
        } catch (Exception e) {
            log.error("读取上片PLC状态异常", e);
            result.put("success", false);
            result.put("message", "获取状态失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量测试 - 模拟完整任务流程
     */
    @PostMapping("/batch/test")
    public Map<String, Object> batchTest(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String protocolType = (String) params.get("protocolType");
            int taskCount = (int) params.getOrDefault("taskCount", 1);
            
            log.info("开始批量测试，协议类型: {}, 任务数量: {}", protocolType, taskCount);
            
            // 模拟完整的任务流程
            for (int i = 1; i <= taskCount; i++) {
                log.info("执行第{}个任务", i);
                
                if ("load".equals(protocolType)) {
                    // 流程
                    plcTestWriteService.simulatePlcRequest(); // 发送请求
                    // 等待MES响应
                    Thread.sleep(1000);
                    plcTestWriteService.simulatePlcReport();  // 任务完成
                    
                } else if ("raw".equals(protocolType)) {
                    // 流程
                    plcTestWriteService.simulatePlcRequest(); // 发送请求
                    // 等待MES响应
                    Thread.sleep(1000);
                    plcTestWriteService.simulatePlcReport();  // 任务完成
                }
                
                // 任务间隔
                if (i < taskCount) {
                    Thread.sleep(2000);
                }
            }
            
            result.put("success", true);
            result.put("message", "批量测试完成，共执行" + taskCount + "个任务");
            
        } catch (Exception e) {
            log.error("批量测试异常", e);
            result.put("success", false);
            result.put("message", "批量测试失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 设置DB块地址
     */
    @PostMapping("/dbBlock")
    public Map<String, Object> setDbBlock(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String dbBlock = (String) params.get("dbBlock");
            plcTestWriteService.setCurrentDbBlock(dbBlock);
            result.put("success", true);
            result.put("message", "DB块地址设置成功: " + dbBlock);
        } catch (Exception e) {
            log.error("设置DB块地址异常", e);
            result.put("success", false);
            result.put("message", "设置DB块地址失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取当前DB块地址
     */
    @GetMapping("/dbBlock")
    public Map<String, Object> getDbBlock() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String dbBlock = plcTestWriteService.getCurrentDbBlock();
            result.put("success", true);
            result.put("data", dbBlock);
            result.put("message", "获取DB块地址成功");
        } catch (Exception e) {
            log.error("获取DB块地址异常", e);
            result.put("success", false);
            result.put("message", "获取DB块地址失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 启用自动测试模式
     */
    @PostMapping("/auto/enable")
    public Map<String, Object> enableAutoTest() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            plcAutoTestService.enableAutoTest();
            result.put("success", true);
            result.put("message", "自动测试模式已启用");
        } catch (Exception e) {
            log.error("启用自动测试模式异常", e);
            result.put("success", false);
            result.put("message", "启用自动测试模式失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 禁用自动测试模式
     */
    @PostMapping("/auto/disable")
    public Map<String, Object> disableAutoTest() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            plcAutoTestService.disableAutoTest();
            result.put("success", true);
            result.put("message", "自动测试模式已禁用");
        } catch (Exception e) {
            log.error("禁用自动测试模式异常", e);
            result.put("success", false);
            result.put("message", "禁用自动测试模式失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取自动测试模式状态
     */
    @GetMapping("/auto/status")
    public Map<String, Object> getAutoTestStatus() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean isEnabled = plcAutoTestService.isAutoTestEnabled();
            result.put("success", true);
            result.put("data", isEnabled);
            result.put("message", "获取自动测试模式状态成功");
        } catch (Exception e) {
            log.error("获取自动测试模式状态异常", e);
            result.put("success", false);
            result.put("message", "获取自动测试模式状态失败: " + e.getMessage());
        }
        
        return result;
    }
}