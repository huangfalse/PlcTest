package com.mes.rawglass.details.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/16 20:24
 * @Description:
 */
@ApiModel(description = "仓储信息汇总")
@Data
public class RawGlassCountVO {

    @ApiModelProperty(value = "设备id", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "工位id", position = 2)
    private Integer slot;

    @ApiModelProperty(value = "架子号", position = 3)
    private Integer shelf;

    @ApiModelProperty(value = "启用/禁用", position = 4)
    private Integer enableState;

    @ApiModelProperty(value = "原片包数", position = 5)
    private Integer rawCount;
}
