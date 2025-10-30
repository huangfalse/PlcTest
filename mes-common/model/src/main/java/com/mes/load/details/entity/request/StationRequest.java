package com.mes.load.details.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上片工位表(LoadStationDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:30:20
 */
@ApiModel(description = "上片工位 请求实体类")
@Data
public class StationRequest {

    @ApiModelProperty("设备id")
    private Integer deviceId;

}

