package com.mes.service.impl;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.entity.PlcBaseData;
import com.mes.entity.PlcAddress;
import com.mes.s7.enhanced.EnhancedS7Serializer;
import com.mes.service.PlcAddressService;
import com.mes.service.IPlcTestWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PLC测试写入服务实现类
 * 模拟PLC行为，向PLC写入测试数据，用于测试MES程序
 * 
 * @author huang
 * @date 2025/10/30
 */
@Slf4j
@Service
public class PlcTestWriteServiceImpl implements PlcTestWriteService {

    @Resource
    private PlcAddressService plcAddressService;

    private static final int ON = 1;
    private static final int OFF = 0;
    
    // 当前使用的项目标识
    private String currentProjectId = "vertical";
    
    // 缓存不同项目的S7Serializer实例
    private final ConcurrentMap<String, EnhancedS7Serializer> serializerCache = new ConcurrentHashMap<>();

    /**
     * 模拟PLC发送请求字（触发MES任务下发）
     */
    @Override
    public boolean simulatePlcRequest() {
        return simulatePlcRequest(currentProjectId);
    }
    
    /**
     * 模拟PLC发送请求字（触发MES任务下发）- 支持指定项目
     */
    @Override
    public boolean simulatePlcRequest(String projectId) {
        try {
            // 获取项目配置（数据库实体）
            PlcAddress config = plcAddressService.getProjectConfigWithMapping(projectId);
            
            // 获取对应的S7Serializer
            EnhancedS7Serializer s7Serializer = getSerializerForProject(projectId, config);
            
            // 读取当前PLC状态
            PlcBaseData currentData = s7Serializer.read(PlcBaseData.class, config.getDbArea(), config.getBeginIndex());

            if (currentData.getOnlineState() == OFF) {
                log.info("当前PLC联机模式为0，停止联机");
                return false;
            }else if (currentData.getPlcReport() == ON){
                log.info("当前上片PLC汇报字为1，重置为0");
                currentData.setPlcReport(OFF);
            }
            // 设置PLC请求字为1（触发MES任务处理）
            currentData.setPlcRequest(ON);
            s7Serializer.write(currentData, config.getDbArea(), config.getBeginIndex());
            log.info("模拟PLC发送请求字成功：plcRequest=1, projectId={}, dbArea={}, beginIndex={}", 
                    projectId, config.getDbArea(), config.getBeginIndex());
            return true;
            
        } catch (Exception e) {
            log.error("模拟PLC请求字失败", e);
            return false;
        }
    }

    /**
     * 模拟PLC任务完成汇报
     */
    @Override
    public boolean simulatePlcReport() {
        return simulatePlcReport(currentProjectId);
    }
    
    /**
     * 模拟PLC任务完成汇报 - 支持指定项目
     */
    @Override
    public boolean simulatePlcReport(String projectId) {
        try {
            // 获取项目配置（数据库实体）
            PlcAddress config = plcAddressService.getProjectConfigWithMapping(projectId);
            
            // 获取对应的S7Serializer
            EnhancedS7Serializer s7Serializer = getSerializerForProject(projectId, config);
            
            PlcBaseData currentData = s7Serializer.read(PlcBaseData.class, config.getDbArea(), config.getBeginIndex());

            // 设置PLC汇报字为1（任务完成）
            currentData.setPlcReport(ON);
            // 请求字清0
            currentData.setPlcRequest(OFF);
            
            // 设置完成数量等数据
            currentData.setMesGlassCount(10);
            
            s7Serializer.write(currentData, config.getDbArea(), config.getBeginIndex());
            log.info("模拟PLC任务完成汇报：plcReport=1, mesGlassCount=10, projectId={}, dbArea={}, beginIndex={}", 
                    projectId, config.getDbArea(), config.getBeginIndex());
            return true;
            
        } catch (Exception e) {
            log.error("模拟PLC任务完成汇报失败", e);
            return false;
        }
    }

    /**
     * 模拟PLC发送联机状态
     */
    @Override
    public boolean simulateOnlineStatus(int onlineState) {
        return simulateOnlineStatus(onlineState, currentProjectId);
    }
    
    /**
     * 模拟PLC发送联机状态 - 支持指定项目
     */
    @Override
    public boolean simulateOnlineStatus(int onlineState, String projectId) {
        try {
            // 获取项目配置（数据库实体）
            PlcAddress config = plcAddressService.getProjectConfigWithMapping(projectId);
            
            // 获取对应的S7Serializer
            EnhancedS7Serializer s7Serializer = getSerializerForProject(projectId, config);
            
            PlcBaseData currentData = s7Serializer.read(PlcBaseData.class, config.getDbArea(), config.getBeginIndex());

            // 1:联机 0:脱机
            currentData.setOnlineState(onlineState);
            
            s7Serializer.write(currentData, config.getDbArea(), config.getBeginIndex());
            log.info("模拟PLC联机状态：onlineState={}, projectId={}, dbArea={}, beginIndex={}", 
                    onlineState, projectId, config.getDbArea(), config.getBeginIndex());
            return true;
            
        } catch (Exception e) {
            log.error("模拟PLC联机状态失败", e);
            return false;
        }
    }

    /**
     * 重置PLC所有状态
     */
    @Override
    public boolean resetPlc() {
        return resetPlc(currentProjectId);
    }
    
    /**
     * 重置PLC所有状态 - 支持指定项目
     */
    @Override
    public boolean resetPlc(String projectId) {
        try {
            // 获取项目配置（数据库实体）
            PlcAddress config = plcAddressService.getProjectConfigWithMapping(projectId);
            
            // 获取对应的S7Serializer
            EnhancedS7Serializer s7Serializer = getSerializerForProject(projectId, config);
            
            PlcBaseData resetData = new PlcBaseData();
            
            // 重置所有关键字段
            resetData.setPlcRequest(OFF);
            resetData.setPlcReport(OFF);
            resetData.setMesSend(OFF);
            resetData.setMesConfirm(OFF);
            // 默认联机
            resetData.setOnlineState(ON);
            resetData.setMesGlassCount(0);
            // 清除报警
            resetData.setAlarmInfo(OFF);

            s7Serializer.write(resetData, config.getDbArea(), config.getBeginIndex());
            log.info("PLC状态已重置, projectId={}, dbArea={}, beginIndex={}", 
                    projectId, config.getDbArea(), config.getBeginIndex());
            return true;
            
        } catch (Exception e) {
            log.error("重置PLC状态失败", e);
            return false;
        }
    }

    /**
     * 读取PLC当前状态
     */
    @Override
    public PlcBaseData readPlcStatus() {
        return readPlcStatus(currentProjectId);
    }
    
    /**
     * 读取PLC当前状态 - 支持指定项目
     */
    @Override
    public PlcBaseData readPlcStatus(String projectId) {
        try {
            // 获取项目配置（数据库实体）
            PlcAddress config = plcAddressService.getProjectConfigWithMapping(projectId);
            
            // 获取对应的S7Serializer
            EnhancedS7Serializer s7Serializer = getSerializerForProject(projectId, config);
            
            return s7Serializer.read(PlcBaseData.class, config.getDbArea(), config.getBeginIndex());
        } catch (Exception e) {
            log.error("读取PLC状态失败", e);
            return null;
        }
    }
    
    /**
     * 设置当前项目标识
     */
    @Override
    public void setCurrentProjectId(String projectId) {
        this.currentProjectId = projectId;
    }
    
    /**
     * 获取当前项目标识
     */
    @Override
    public String getCurrentProjectId() {
        return this.currentProjectId;
    }
    
    /**
     * 获取项目对应的S7Serializer实例
     * 如果不存在，则创建一个新的实例并缓存
     * 
     * @param projectId 项目标识
     * @param config 项目配置
     * @return S7Serializer实例
     */
    private EnhancedS7Serializer getSerializerForProject(String projectId, PlcAddress config) {
        return serializerCache.computeIfAbsent(projectId, id -> {
            // 解析PLC类型
            EPlcType plcType = EPlcType.S1200; // 默认值
            if (config != null && config.getPlcType() != null) {
                try {
                    plcType = EPlcType.valueOf(config.getPlcType());
                } catch (IllegalArgumentException e) {
                    log.warn("未知的PLC类型: {}, 使用默认类型 S1200", config.getPlcType());
                }
            }
            
            // 创建S7PLC实例
            String plcIp = (config != null && config.getPlcIp() != null) ? config.getPlcIp() : "192.168.10.21";
            S7PLC s7Plc = new S7PLC(plcType, plcIp);
            
            // 创建并返回EnhancedS7Serializer实例
            return EnhancedS7Serializer.newInstance(s7Plc);
        });
    }
    
    /**
     * 清除指定项目的S7Serializer缓存
     * 
     * @param projectId 项目标识
     */
    @Override
    public void clearSerializerCache(String projectId) {
        serializerCache.remove(projectId);
        log.info("已清除项目 {} 的S7Serializer缓存", projectId);
    }
    
    /**
     * 清除所有S7Serializer缓存
     */
    @Override
    public void clearAllSerializerCache() {
        serializerCache.clear();
        log.info("已清除所有S7Serializer缓存");
    }
}