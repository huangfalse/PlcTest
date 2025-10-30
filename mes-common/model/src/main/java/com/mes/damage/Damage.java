package com.mes.damage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wu
 * @since 2024-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Damage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程号")
    private String engineerId;

    @ApiModelProperty(value = "玻璃id")
    private String glassId;

    @ApiModelProperty(value = "线路")
    private Integer line;

    @ApiModelProperty(value = "工序")
    private String workingProcedure;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "报工状态：1：未报工 3：已报工 7未报工已现补  8同步现补给erp")
    private Integer reportState;

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    @ApiModelProperty(value = "层")
    private Integer layer;

    @ApiModelProperty(value = "班组名称")
    private String teamsGroupsName;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "破损详情id")
    private Long damageDetailsId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String updateTime;

}
