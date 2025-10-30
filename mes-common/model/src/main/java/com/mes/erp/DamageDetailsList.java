package com.mes.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wu
 * @since 2024-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DamageDetailsList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <次破明细表>
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报工编号
     */
    private String reportingWorkId;

    /**
     * 流程卡号
     */
    private String processId;

    /**
     * 序号
     */
    private Integer orderNumber;

    /**
     * 工艺确认序号（层号）
     */
    private Integer technologyNumber;

    /**
     * 次破数量
     */
    private Integer breakageQuantity;

    /**
     * 次破类型
     */
    private String breakageType;

    /**
     * 次破原因
     */
    private String breakageReason;

    /**
     * 可利用（默认0，需要返工1）
     */
    private Integer available;

    /**
     * 返工/补片数量
     */
    private Integer quantity;

    /**
     * 返工工序
     */
    private String returnProcess;

    /**
     * 责任工序
     */
    private String responsibleProcess;

    /**
     * 责任人员
     */
    private String responsiblePersonnel;

    /**
     * 责任班组
     */
    private String responsibleTeam;

    /**
     * 责任设备
     */
    private String responsibleEquipment;

    /**
     * 质检员
     */
    private String qualityInspector;

    /**
     * 补片状态（默认0，已补片1）
     */
    private Integer patchStatus;

    /**
     * 质检状态（默认0,1待质检1，已质检2）
     */
    private Integer qualityInsStatus;


}
