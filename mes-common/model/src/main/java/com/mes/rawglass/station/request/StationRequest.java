package com.mes.rawglass.station.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huang
 * @since 2025/7/17
 */
@ApiModel(description = "")
@Data
public class StationRequest {
    @ApiModelProperty(value ="工位状态",position = 1)
    private Integer enableState;

    @ApiModelProperty(value = "栅格号", position = 2)
    private Integer slot;
}
