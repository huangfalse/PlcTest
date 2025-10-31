package com.mes.controller;

import com.mes.service.PlcAutoTestService;
import com.mes.service.PlcTestWriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PLC测试任务控制器
 * 提供PLC测试任务管理相关的API接口
 * <p>
 * 实现前端Test.vue组件所需的所有API接口
 * 
 * @author huang
 * @date 2025/10/30
 */
@Slf4j
@RestController
@RequestMapping("/api/plc/test")
@Api(tags = "PLC测试任务管理")
public class PlcTestController {

    @Resource
    private PlcTestWriteService plcTestWriteService;

    @Resource
    private PlcAutoTestService plcAutoTestService;

    // 模拟任务数据存储
    private final Map<String, Map<String, Object>> taskMap = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> moduleStatusMap = new ConcurrentHashMap<>();

    /**
     * 执行指定模块的测试任务
     * 对应前端：submitTest
     */
    @PostMapping("/execute/{module}")
    @ApiOperation("执行指定模块的测试任务")
    public Map<String, Object> executeTest(@ApiParam("模块名称") @PathVariable String module) {
        log.info("执行测试任务，模块: {}", module);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 模拟执行测试任务
            boolean success = plcTestWriteService.simulatePlcRequest(module);
            
            // 创建任务记录
            String taskId = UUID.randomUUID().toString();
            Map<String, Object> task = new HashMap<>();
            task.put("id", taskId);
            task.put("module", module);
            task.put("status", success ? "running" : "failed");
            task.put("createTime", new Date());
            task.put("message", success ? "测试任务已启动" : "测试任务启动失败");
            
            taskMap.put(taskId, task);
            
            // 更新模块状态
            updateModuleStatus(module, "running");
            
            result.put("success", success);
            result.put("data", taskId);
            result.put("message", success ? "测试任务已启动" : "测试任务启动失败");
        } catch (Exception e) {
            log.error("执行测试任务异常，模块: {}", module, e);
            result.put("success", false);
            result.put("message", "执行测试任务异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取指定模块的任务状态
     * 对应前端：getTaskStatus
     */
    @GetMapping("/status/{module}")
    @ApiOperation("获取指定模块的任务状态")
    public Map<String, Object> getTaskStatus(@ApiParam("模块名称") @PathVariable String module) {
        log.info("获取任务状态，模块: {}", module);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 查找该模块的最新任务
            Optional<Map.Entry<String, Map<String, Object>>> latestTask =
                    taskMap.entrySet().stream()
                            .filter(entry -> module.equals(entry.getValue().get("module")))
                            .max(Map.Entry.comparingByKey());
            
            if (latestTask.isPresent()) {
                Map<String, Object> task = latestTask.get().getValue();
                
                // 模拟任务状态变化
                String status = (String) task.get("status");
                if ("running".equals(status)) {
                    // 随机决定任务是否完成
                    if (Math.random() > 0.5) {
                        task.put("status", "success");
                        task.put("message", "测试任务执行成功");
                        updateModuleStatus(module, "idle");
                    }
                }
                
                result.put("success", true);
                result.put("data", task);
            } else {
                result.put("success", false);
                result.put("message", "未找到模块的任务记录");
            }
        } catch (Exception e) {
            log.error("获取任务状态异常，模块: {}", module, e);
            result.put("success", false);
            result.put("message", "获取任务状态异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 批量测试
     * 对应前端：batchTest
     */
    @PostMapping("/batch")
    @ApiOperation("批量测试")
    public Map<String, Object> batchTest(@ApiParam("批量测试请求") @RequestBody Map<String, Object> request) {
        List<String> modules = (List<String>) request.get("modules");
        log.info("执行批量测试，模块列表: {}", modules);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            for (String module : modules) {
                Map<String, Object> moduleResult = new HashMap<>();
                moduleResult.put("module", module);
                
                try {
                    boolean success = plcTestWriteService.simulatePlcRequest(module);
                    moduleResult.put("success", success);
                    moduleResult.put("message", success ? "测试任务已启动" : "测试任务启动失败");
                    
                    if (success) {
                        updateModuleStatus(module, "running");
                    }
                } catch (Exception e) {
                    moduleResult.put("success", false);
                    moduleResult.put("message", "执行测试异常: " + e.getMessage());
                    log.error("批量测试模块异常，模块: {}", module, e);
                }
                
                results.add(moduleResult);
            }
            
            result.put("success", true);
            result.put("data", results);
            result.put("message", "批量测试任务已提交");
        } catch (Exception e) {
            log.error("批量测试异常", e);
            result.put("success", false);
            result.put("message", "批量测试异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 导入测试任务
     * 对应前端：importTask
     */
    @PostMapping("/import")
    @ApiOperation("导入测试任务")
    public Map<String, Object> importTask(@ApiParam("任务文件") @RequestParam("file") MultipartFile file) {
        log.info("导入测试任务文件: {}", file.getOriginalFilename());
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 这里只是模拟导入功能
            // 实际应该解析文件内容，创建测试任务
            
            result.put("success", true);
            result.put("data", file.getOriginalFilename());
            result.put("message", "测试任务导入成功");
        } catch (Exception e) {
            log.error("导入测试任务异常", e);
            result.put("success", false);
            result.put("message", "导入测试任务异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取所有模块状态
     * 对应前端：getModulesStatus
     */
    @GetMapping("/modules/status")
    @ApiOperation("获取所有模块状态")
    public Map<String, Object> getModulesStatus() {
        log.info("获取所有模块状态");
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 初始化默认模块状态（如果不存在）
            initDefaultModuleStatus();
            
            // 更新所有模块状态
            for (String module : moduleStatusMap.keySet()) {
                checkModuleStatus(module);
            }
            
            result.put("success", true);
            result.put("data", new ArrayList<>(moduleStatusMap.values()));
        } catch (Exception e) {
            log.error("获取模块状态异常", e);
            result.put("success", false);
            result.put("message", "获取模块状态异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 重置指定模块
     * 对应前端：resetModule
     */
    @PostMapping("/reset/{module}")
    @ApiOperation("重置指定模块")
    public Map<String, Object> resetModule(@ApiParam("模块名称") @PathVariable String module) {
        log.info("重置模块: {}", module);
        
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = plcTestWriteService.resetPlc(module);
            
            if (success) {
                updateModuleStatus(module, "idle");
                // 清除该模块的所有任务
                taskMap.entrySet().removeIf(entry -> module.equals(entry.getValue().get("module")));
            }
            
            result.put("success", success);
            result.put("message", success ? "模块重置成功" : "模块重置失败");
        } catch (Exception e) {
            log.error("重置模块异常，模块: {}", module, e);
            result.put("success", false);
            result.put("message", "重置模块异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 重试指定模块的任务
     * 对应前端：retryTask
     */
    @PostMapping("/retry/{module}")
    @ApiOperation("重试指定模块的任务")
    public Map<String, Object> retryTask(@ApiParam("模块名称") @PathVariable String module) {
        log.info("重试模块任务: {}", module);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 先重置模块
            plcTestWriteService.resetPlc(module);
            // 然后重新执行测试
            boolean success = plcTestWriteService.simulatePlcRequest(module);
            
            // 创建新任务记录
            String taskId = UUID.randomUUID().toString();
            Map<String, Object> task = new HashMap<>();
            task.put("id", taskId);
            task.put("module", module);
            task.put("status", success ? "running" : "failed");
            task.put("createTime", new Date());
            task.put("message", success ? "测试任务已重启" : "测试任务重启失败");
            
            taskMap.put(taskId, task);
            
            if (success) {
                updateModuleStatus(module, "running");
            }
            
            result.put("success", success);
            result.put("data", taskId);
            result.put("message", success ? "测试任务已重启" : "测试任务重启失败");
        } catch (Exception e) {
            log.error("重试模块任务异常，模块: {}", module, e);
            result.put("success", false);
            result.put("message", "重试模块任务异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 停止指定模块的测试
     * 对应前端：stopTest
     */
    @PostMapping("/stop/{module}")
    @ApiOperation("停止指定模块的测试")
    public Map<String, Object> stopTest(@ApiParam("模块名称") @PathVariable String module) {
        log.info("停止模块测试: {}", module);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 重置PLC状态来停止测试
            boolean success = plcTestWriteService.resetPlc(module);
            
            if (success) {
                updateModuleStatus(module, "idle");
                // 更新该模块的所有运行中任务状态
                taskMap.entrySet().stream()
                        .filter(entry -> module.equals(entry.getValue().get("module")) && "running".equals(entry.getValue().get("status")))
                        .forEach(entry -> {
                            entry.getValue().put("status", "stopped");
                            entry.getValue().put("message", "测试任务已手动停止");
                        });
            }
            
            result.put("success", success);
            result.put("message", success ? "测试已停止" : "停止测试失败");
        } catch (Exception e) {
            log.error("停止模块测试异常，模块: {}", module, e);
            result.put("success", false);
            result.put("message", "停止模块测试异常: " + e.getMessage());
        }
        return result;
    }

    // 初始化默认模块状态
    private void initDefaultModuleStatus() {
        // 定义默认的模块列表
        List<String> defaultModules = Arrays.asList(
                "gantryStorage",
                "upperModule",
                "lowerModule",
                "stackerModule",
                "shuttleModule",
                "verticalModule",
                "horizontalModule"
        );
        
        for (String module : defaultModules) {
            moduleStatusMap.computeIfAbsent(module, k -> {
                Map<String, Object> status = new HashMap<>();
                status.put("module", module);
                status.put("status", "idle");
                status.put("updateTime", new Date());
                return status;
            });
        }
    }

    // 更新模块状态
    private void updateModuleStatus(String module, String status) {
        Map<String, Object> moduleStatus = moduleStatusMap.computeIfAbsent(module, k -> new HashMap<>());
        moduleStatus.put("module", module);
        moduleStatus.put("status", status);
        moduleStatus.put("updateTime", new Date());
    }

    // 检查并更新模块状态
    private void checkModuleStatus(String module) {
        Map<String, Object> moduleStatus = moduleStatusMap.get(module);
        if (moduleStatus != null) {
            String status = (String) moduleStatus.get("status");
            // 如果模块状态为running，可以在这里添加逻辑来检查实际状态
            // 这里简单模拟状态变化
            if ("running".equals(status) && Math.random() > 0.7) {
                moduleStatus.put("status", "idle");
                moduleStatus.put("updateTime", new Date());
            }
        }
    }
}