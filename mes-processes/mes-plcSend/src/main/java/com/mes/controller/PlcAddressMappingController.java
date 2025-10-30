package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.config.PlcAddressMappingConfig;
import com.mes.entity.PlcAddressMapping;
import com.mes.service.PlcAddressMappingService;
import com.mes.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PLC地址映射配置控制器
 * 提供PLC地址映射配置的增删改查接口
 * 
 * @author zhoush
 * @date 2025/10/29
 */
@Slf4j
@RestController
@RequestMapping("/api/plc/address-mapping")
@Api(tags = "PLC地址映射配置管理")
public class PlcAddressMappingController {

    @Resource
    private PlcAddressMappingService plcAddressMappingService;

    /**
     * 获取所有PLC地址映射配置
     */
    @GetMapping("/list")
    @ApiOperation("获取所有PLC地址映射配置")
    public Result<List<PlcAddressMapping>> getAllMappings() {
        try {
            List<PlcAddressMapping> mappings = plcAddressMappingService.getAllMappings();
            return Result.success(mappings);
        } catch (Exception e) {
            log.error("获取PLC地址映射配置列表失败", e);
            return Result.error("获取PLC地址映射配置列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页获取PLC地址映射配置
     */
    @GetMapping("/page")
    @ApiOperation("分页获取PLC地址映射配置")
    public Result<IPage<PlcAddressMapping>> getMappingsByPage(
            @ApiParam("页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") int size,
            @ApiParam("项目标识，支持模糊查询") @RequestParam(required = false) String projectId,
            @ApiParam("PLC IP地址，支持模糊查询") @RequestParam(required = false) String plcIp) {
        try {
            IPage<PlcAddressMapping> pageResult = plcAddressMappingService.getMappingsByPage(page, size, projectId, plcIp);
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("分页获取PLC地址映射配置失败", e);
            return Result.error("分页获取PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取PLC地址映射配置
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取PLC地址映射配置")
    public Result<PlcAddressMapping> getMappingById(
            @ApiParam("配置ID") @PathVariable Long id) {
        try {
            PlcAddressMapping mapping = plcAddressMappingService.getMappingById(id);
            if (mapping != null) {
                return Result.success(mapping);
            } else {
                return Result.error("未找到ID为 " + id + " 的PLC地址映射配置");
            }
        } catch (Exception e) {
            log.error("获取PLC地址映射配置失败，ID: " + id, e);
            return Result.error("获取PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据项目标识获取PLC地址映射配置
     */
    @GetMapping("/project/{projectId}")
    @ApiOperation("根据项目标识获取PLC地址映射配置")
    public Result<PlcAddressMapping> getMappingByProjectId(
            @ApiParam("项目标识") @PathVariable String projectId) {
        try {
            PlcAddressMapping mapping = plcAddressMappingService.getMappingByProjectId(projectId);
            if (mapping != null) {
                return Result.success(mapping);
            } else {
                return Result.error("未找到项目标识为 " + projectId + " 的PLC地址映射配置");
            }
        } catch (Exception e) {
            log.error("获取PLC地址映射配置失败，项目标识: " + projectId, e);
            return Result.error("获取PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 根据项目标识获取项目配置（包含地址映射）
     */
    @GetMapping("/project/{projectId}/config")
    @ApiOperation("根据项目标识获取项目配置（包含地址映射）")
    public Result<PlcAddressMappingConfig.ProjectPlcConfig> getProjectConfig(
            @ApiParam("项目标识") @PathVariable String projectId) {
        try {
            PlcAddressMappingConfig.ProjectPlcConfig config = plcAddressMappingService.getProjectConfig(projectId);
            if (config != null) {
                return Result.success(config);
            } else {
                return Result.error("未找到项目标识为 " + projectId + " 的PLC配置");
            }
        } catch (Exception e) {
            log.error("获取项目PLC配置失败，项目标识: " + projectId, e);
            return Result.error("获取项目PLC配置失败: " + e.getMessage());
        }
    }

    /**
     * 创建新的PLC地址映射配置
     */
    @PostMapping
    @ApiOperation("创建新的PLC地址映射配置")
    public Result<PlcAddressMapping> createMapping(
            @ApiParam("PLC地址映射配置") @RequestBody PlcAddressMapping mapping) {
        try {
            // 参数验证
            if (mapping.getProjectId() == null || mapping.getProjectId().trim().isEmpty()) {
                return Result.error("项目标识不能为空");
            }
            if (mapping.getDbArea() == null || mapping.getDbArea().trim().isEmpty()) {
                return Result.error("DB块地址不能为空");
            }
            if (mapping.getBeginIndex() == null) {
                mapping.setBeginIndex(0); // 默认起始索引为0
            }

            PlcAddressMapping created = plcAddressMappingService.saveMapping(mapping);
            log.info("创建PLC地址映射配置成功，ID: {}, 项目标识: {}", created.getId(), created.getProjectId());
            return Result.success(created);
        } catch (Exception e) {
            log.error("创建PLC地址映射配置失败", e);
            return Result.error("创建PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 更新PLC地址映射配置
     */
    @PutMapping("/{id}")
    @ApiOperation("更新PLC地址映射配置")
    public Result<PlcAddressMapping> updateMapping(
            @ApiParam("配置ID") @PathVariable Long id,
            @ApiParam("更新的PLC地址映射配置") @RequestBody PlcAddressMapping mapping) {
        try {
            // 参数验证
            if (mapping.getProjectId() == null || mapping.getProjectId().trim().isEmpty()) {
                return Result.error("项目标识不能为空");
            }
            if (mapping.getDbArea() == null || mapping.getDbArea().trim().isEmpty()) {
                return Result.error("DB块地址不能为空");
            }

            mapping.setId(id);
            PlcAddressMapping updated = plcAddressMappingService.updateMapping(mapping);
            if (updated != null) {
                log.info("更新PLC地址映射配置成功，ID: {}, 项目标识: {}", updated.getId(), updated.getProjectId());
                return Result.success(updated);
            } else {
                return Result.error("未找到ID为 " + id + " 的PLC地址映射配置");
            }
        } catch (Exception e) {
            log.error("更新PLC地址映射配置失败，ID: " + id, e);
            return Result.error("更新PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 删除PLC地址映射配置
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除PLC地址映射配置")
    public Result<Void> deleteMapping(
            @ApiParam("配置ID") @PathVariable Long id) {
        try {
            boolean deleted = plcAddressMappingService.deleteMapping(id);
            if (deleted) {
                log.info("删除PLC地址映射配置成功，ID: {}", id);
                return Result.success();
            } else {
                return Result.error("未找到ID为 " + id + " 的PLC地址映射配置");
            }
        } catch (Exception e) {
            log.error("删除PLC地址映射配置失败，ID: " + id, e);
            return Result.error("删除PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除PLC地址映射配置
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除PLC地址映射配置")
    public Result<Void> deleteMappings(
            @ApiParam("配置ID列表") @RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("ID列表不能为空");
            }
            
            int deletedCount = plcAddressMappingService.deleteMappings(ids);
            log.info("批量删除PLC地址映射配置成功，删除数量: {}, ID列表: {}", deletedCount, ids);
            return Result.success();
        } catch (Exception e) {
            log.error("批量删除PLC地址映射配置失败，ID列表: " + ids, e);
            return Result.error("批量删除PLC地址映射配置失败: " + e.getMessage());
        }
    }

    /**
     * 测试PLC连接
     */
    @PostMapping("/{id}/test-connection")
    @ApiOperation("测试PLC连接")
    public Result<String> testConnection(
            @ApiParam("配置ID") @PathVariable Long id) {
        try {
            PlcAddressMapping mapping = plcAddressMappingService.getMappingById(id);
            if (mapping == null) {
                return Result.error("未找到ID为 " + id + " 的PLC地址映射配置");
            }

            boolean isConnected = plcAddressMappingService.testConnection(mapping);
            if (isConnected) {
                String message = String.format("PLC连接测试成功 - 项目: %s, IP: %s, DB块: %s", 
                        mapping.getProjectId(), mapping.getPlcIp(), mapping.getDbArea());
                log.info(message);
                return Result.success(message);
            } else {
                String message = String.format("PLC连接测试失败 - 项目: %s, IP: %s, DB块: %s", 
                        mapping.getProjectId(), mapping.getPlcIp(), mapping.getDbArea());
                log.warn(message);
                return Result.error(message);
            }
        } catch (Exception e) {
            log.error("PLC连接测试失败，ID: " + id, e);
            return Result.error("PLC连接测试失败: " + e.getMessage());
        }
    }

    /**
     * 重新加载配置文件中的地址映射
     */
    @PostMapping("/reload-config")
    @ApiOperation("重新加载配置文件中的地址映射")
    public Result<String> reloadConfig() {
        try {
            plcAddressMappingService.reloadConfigMappings();
            String message = "配置文件中的PLC地址映射已重新加载";
            log.info(message);
            return Result.success(message);
        } catch (Exception e) {
            log.error("重新加载配置文件失败", e);
            return Result.error("重新加载配置文件失败: " + e.getMessage());
        }
    }
}