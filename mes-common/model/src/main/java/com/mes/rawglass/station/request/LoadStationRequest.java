package com.mes.rawglass.station.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上片工位表(LoadStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:32
 */
@ApiModel(description = "上片工位数据请求")
@Data
public class LoadStationRequest {

    @ApiModelProperty(value ="设备id",position = 1)
    private Integer deviceId;

}

