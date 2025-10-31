package com.mes.service.impl;

import com.mes.service.IPlcAutoTestService;
import com.mes.service.IPlcTestWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * PLC自动测试服务实现类
 * 定时模拟PLC行为，向PLC写入测试数据，用于自动测试MES程序
 *
 * @author huang
 * @date 2025/10/30
 */
@Slf4j
@Service
public class PlcAutoTestServiceImpl implements PlcAutoTestService {

    @Resource
    private IPlcTestWriteService plcTestWriteService;

    // 自动测试开关
    @Value("${plc.auto.test.enabled:false}")
    private boolean autoTestEnabled;

    // 自动测试间隔（毫秒）
    @Value("${plc.auto.test.interval:5000}")
    private long autoTestInterval;

    /**
     * 定时自动测试任务
     */
    @Scheduled(fixedDelayString = "${plc.auto.test.interval:5000}")
    public void autoTestTask() {
        if (!autoTestEnabled) {
            return;
        }

        try {
            log.info("开始执行PLC自动测试任务");
            // 模拟PLC请求
            plcTestWriteService.simulatePlcRequest();
            Thread.sleep(1000);
            // 模拟PLC汇报
            plcTestWriteService.simulatePlcReport();
            log.info("PLC自动测试任务执行完成");
        } catch (Exception e) {
            log.error("PLC自动测试任务执行异常", e);
        }
    }

    /**
     * 启用自动测试
     */
    @Override
    public void enableAutoTest() {
        this.autoTestEnabled = true;
        log.info("PLC自动测试已启用");
    }

    /**
     * 禁用自动测试
     */
    @Override
    public void disableAutoTest() {
        this.autoTestEnabled = false;
        log.info("PLC自动测试已禁用");
    }

    /**
     * 获取自动测试状态
     */
    @Override
    public boolean isAutoTestEnabled() {
        return this.autoTestEnabled;
    }
}