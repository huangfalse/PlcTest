package com.mes.tempering.compute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/23 13:21
 * @Description:
 */
@Data
public class TemperingFlowCardIdCountVO {

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "层数")
    private Integer layer;

    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    @ApiModelProperty(value = "小片数量")
    private Integer glassCount;
}
