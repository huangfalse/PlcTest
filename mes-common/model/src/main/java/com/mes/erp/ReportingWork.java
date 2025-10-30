package com.mes.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wu
 * @since 2024-06-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportingWork implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <报工主表>
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报工编号
     */
    private String reportingWorkId = "";

    /**
     * 销售单号
     */
    private String orderId;

    /**
     * 生产订单号
     */
    private String productionId;

    /**
     * 流程卡号
     */
    private String processId;

    /**
     * 本工序
     */
    private String thisProcess;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 本工序数量
     */
    private Integer thisProcessQuantity;

    /**
     * 本工序完工数
     */
    private Integer thisCompletedQuantity;

    /**
     * 本工序次破数
     */
    private Integer thisWornQuantity;

    /**
     * 下工序
     */
    private String nextProcess;

    /**
     * 班次
     */
    private String classes;

    /**
     * 班组编号
     */
    private String teamsGroupsId;

    /**
     * 班组名称
     */
    private String teamsGroupsName;

    /**
     * 报工时间
     */
    private LocalDateTime reportingWorkTime;

    /**
     * 创建者id
     */
    private String creatorId;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 建立时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
