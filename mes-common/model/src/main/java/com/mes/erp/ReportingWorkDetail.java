package com.mes.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wu
 * @since 2024-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportingWorkDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * <报工明细>
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报工编号
     */
    private String reportingWorkId;

    /**
     * 序号
     */
    private Integer orderNumber;

    /**
     * 工艺确认序号
     */
    private Integer technologyNumber;

    /**
     * 完工数量
     */
    private Integer completedQuantity;

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
     * 可利用（1可利用0默认）
     */
    private Integer available;

    /**
     * 退回工序
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
     * 补片状态
     */
    private Integer patchStatus;

    /**
     * 在制品名称
     */
    private String workProgress;

    /**
     * 质检状态
     */
    private Integer qualityInsStatus;

    /**
     * 报工类型
     */
    private Integer type;

    /**
     * 待返工数量
     */
    private Integer waitReworkQuantity;

    /**
     * 返工班组
     */
    private String reworkTeam;

    /**
     * 返工审核人
     */
    private String reviewer;

    /**
     * 返工审核状态
     */
    private String reviewStatus;

    /**
     * 返工时间
     */
    private LocalDateTime reworkTime;

    /**
     * 返工修改时间
     */
    private LocalDateTime reworkUpdateTime;

    private List<DamageDetailsList> damageDetails;

}
