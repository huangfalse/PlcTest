package com.mes.rawglass.details.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/7 11:10
 * @Description:
 */
@ApiModel(description = "仓储信息")
@Data
public class RawGlassDetailsDTO {

    @ApiModelProperty("任务数量")
    private Integer taskCount;

    @ApiModelProperty("开始位置")
    private Integer startSlot;

    @ApiModelProperty("靠边位置")
    private Integer rawMarginOne;

    @ApiModelProperty("靠边位置")
    private Integer rawMarginTwo;

}
