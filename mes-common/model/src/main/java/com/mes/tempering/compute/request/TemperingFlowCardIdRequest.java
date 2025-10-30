package com.mes.tempering.compute.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 19:13
 * @Description:
 */
@Data
public class TemperingFlowCardIdRequest {

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "层")
    private Integer layer;

    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    @ApiModelProperty(value = "计算数量")
    private Integer glassCount;

    @ApiModelProperty(value = "幕墙模式:curtain_wall")
    private Boolean curtainWall;

    @ApiModelProperty("旋转模式:rotate_mode")
    private Boolean rotateMode;
}
