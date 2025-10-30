package com.mes.rawglass.station.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 8:55
 * @Description:
 */
@ApiModel(description = "格子状态")
@Data
public class UpdateStorageRequest {

    @ApiModelProperty(value = "启用/禁用", position = 1)
    private Integer enableState;

    @ApiModelProperty(value = "架子号", position = 2)
    private Integer shelf;
}
