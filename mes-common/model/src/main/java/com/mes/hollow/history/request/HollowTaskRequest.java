package com.mes.hollow.history.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "中空任务接口参数")
@Data
public class HollowTaskRequest {

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "路线")
    private Integer cell;

    @ApiModelProperty(value = "配对数量")
    private Integer totalPairQuantity;

    @ApiModelProperty(value = "配方id")
    private Integer formulaId;

    @ApiModelProperty(value = "是否强制出片")
    private Integer isForce;

    @ApiModelProperty(value = "是否已出")
    private Integer isOut;

}


