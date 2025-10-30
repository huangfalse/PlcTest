package com.mes.edging.job.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/22 8:43
 * @Description:
 */
@ApiModel(description = "任务重发入参")
@Data
public class EdgJobRequest {

    @ApiModelProperty(value = "玻璃id", position = 1)
    private String glassId;

    @ApiModelProperty(value = "宽*10", position = 2)
    private double width;

    @ApiModelProperty(value = "高*10", position = 3)
    private double height;

    @ApiModelProperty(value = "厚度*10", position = 4)
    private double thickness;

    @ApiModelProperty(value = "玻璃id对应标识", position = 5)
    private String glassIdKey;
}
