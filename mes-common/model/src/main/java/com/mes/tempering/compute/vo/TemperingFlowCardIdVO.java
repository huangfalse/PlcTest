package com.mes.tempering.compute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/23 13:21
 * @Description:
 */
@Data
public class TemperingFlowCardIdVO {

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "宽")
    private Double width;

    @ApiModelProperty(value = "高")
    private Double height;

    @ApiModelProperty(value = "厚度")
    private Double thickness;

    @ApiModelProperty(value = "膜系")
    private String filmsId;

    @ApiModelProperty(value = "层数")
    private Integer layer;

    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    @ApiModelProperty(value = "笼内可排产数量")
    private Integer realCount;

    @ApiModelProperty(value = "破损数量")
    private Integer damageCount;

    @ApiModelProperty(value = "待排产数量")
    private Integer needTemperingCount;

    @ApiModelProperty(value = "已排产数量")
    private Integer computeCount;

}
