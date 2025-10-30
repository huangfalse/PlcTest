package com.mes.vertical.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/23 10:48
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class HollowRequest {

    @ApiModelProperty(value = "设备id", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "流程卡", position = 2)
    private String flowCardId;

    @ApiModelProperty(value = "膜系", position = 3)
    private String filmsId;

    @ApiModelProperty(value = "厚度", position = 4)
    private Integer thickness;

    @ApiModelProperty(value = "客户名称", position = 5)
    private String customerName;

    @ApiModelProperty(value = "产品名称", position = 6)
    private String productName;

    @ApiModelProperty(value = "总层数", position = 7)
    private Integer totalLayer;

    @ApiModelProperty(value = "总配对数", position = 8)
    private Integer totalPairQuantity;

    @ApiModelProperty(value = "是否强制出片", position = 9)
    private Integer isOut;

    @ApiModelProperty(value = "线路", position = 10)
    private Integer cell;

}
