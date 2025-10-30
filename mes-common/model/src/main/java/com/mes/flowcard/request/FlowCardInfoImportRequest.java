package com.mes.flowcard.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 11:11
 * @Description:
 */

@ApiModel(description = "流程卡信息 实体类")
@Data
public class FlowCardInfoImportRequest {

    @ApiModelProperty(value = "流程卡", position = 2)
    private String flowCardId;

    @ApiModelProperty(value = "宽", position = 3)
    private Double width;

    @ApiModelProperty(value = "高", position = 4)
    private Double height;

    @ApiModelProperty(value = "厚度", position = 5)
    private Double thickness;

    @ApiModelProperty(value = "膜系", position = 6)
    private String filmsId;

    @ApiModelProperty(value = "总层数", position = 7)
    private Integer totalLayer;

    @ApiModelProperty(value = "层数", position = 8)
    private Integer layer;

    @ApiModelProperty(value = "小片总数", position = 9)
    private Integer glassTotal;

    @ApiModelProperty(value = "订单号", position = 10)
    private String orderNumber;

    @ApiModelProperty(value = "产品名称", position = 11)
    private String productName;

    @ApiModelProperty(value = "客户名称", position = 12)
    private String customerName;

}