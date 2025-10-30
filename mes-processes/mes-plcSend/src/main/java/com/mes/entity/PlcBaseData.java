package com.mes.entity;

import com.github.xingshuangs.iot.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.serializer.S7Variable;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author huang
 * @since 2025/10/30
 */
@Data
@Component
public class PlcBaseData {
    /**
     * plc
     * 工作模式：1mes模式 0单机模式
     */
    @S7Variable(address = "onlineState", type = EDataType.UINT16)
    private Integer onlineState;

    /**
     * 任务请求 0无情求 1有请求
     */
    @S7Variable(address = "plcRequest", type = EDataType.UINT16)
    private Integer plcRequest;

    /**
     * 任务汇报 0无汇报 1汇报 3未完成
     */
    @S7Variable(address = "plcReport", type = EDataType.UINT16)
    private Integer plcReport;

    /**
     * mes控制
     * 启动字  1启动 完成后 无情求时清0
     */
    @S7Variable(address = "mesSend", type = EDataType.UINT16)
    private Integer mesSend;

    /**
     * 确认字
     */
    @S7Variable(address = "mesConfirm", type = EDataType.UINT16)
    private Integer mesConfirm;

    /**
     * 玻璃数量
     */
    @S7Variable(address = "mesGlassCount", type = EDataType.UINT16)
    private Integer mesGlassCount;

    /**
     * 报警信号
     */
    @S7Variable(address = "alarmInfo", type = EDataType.UINT16)
    private Integer alarmInfo;

}