package com.mes.horizontal.station.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "卧理笼子")
@Data
public class StationRequest {

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("栅格号")
    private Integer slot;

}


