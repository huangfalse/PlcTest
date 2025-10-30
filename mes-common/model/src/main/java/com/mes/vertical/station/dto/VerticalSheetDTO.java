package com.mes.vertical.station.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/17 20:27
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class VerticalSheetDTO {

    @ApiModelProperty(value = "格子号", position = 1)
    private Integer slot;

    @ApiModelProperty(value = "剩余宽度", position = 2)
    private Integer width;

    @ApiModelProperty(value = "玻璃数量", position = 3)
    private Integer glassCount;

    @ApiModelProperty(value = "笼子号", position = 4)
    private Integer deviceId;

    @ApiModelProperty(value = "虚拟炉号", position = 5)
    private Integer virtualTemperingLayoutId;

    @ApiModelProperty(value = "虚拟组号", position = 5)
    private Integer virtualSlot;

    @ApiModelProperty(value = "序号", position = 6)
    private Integer slotSequence;

    @ApiModelProperty(value = "边距", position = 7)
    private Integer margin;
}
