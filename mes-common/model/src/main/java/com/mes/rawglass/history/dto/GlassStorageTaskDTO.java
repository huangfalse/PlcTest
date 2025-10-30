package com.mes.rawglass.history.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huang
 * @since 2025/7/17
 */
@Data
public class GlassStorageTaskDTO {

    @ApiModelProperty(value = "起始格子", position = 1)
    private Integer startSlot;

    @ApiModelProperty(value = "目标格子", position = 2)
    private Integer endSlot;

    @ApiModelProperty(value = "架子号", position = 3)
    private Integer shelf;

    @ApiModelProperty(value = "原片数量", position = 4)
    private Integer rawQuantity;

    @ApiModelProperty(value = "任务类型", position = 5)
    private Integer taskType;
}
