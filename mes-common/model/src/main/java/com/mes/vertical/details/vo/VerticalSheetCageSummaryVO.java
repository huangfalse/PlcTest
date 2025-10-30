package com.mes.vertical.details.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/30 13:32
 * @Description:
 */
@ApiModel(description = "大理片界面汇总")
@Data
public class VerticalSheetCageSummaryVO {

    @ApiModelProperty(value = "工程号", position = 1)
    private String engineerId;

    @ApiModelProperty(value = "笼内总炉数", position = 2)
    private Integer countTemp;

    @ApiModelProperty(value = "笼内玻璃数量", position = 3)
    private Integer countGlass;

    @ApiModelProperty(value = "面积", position = 4)
    private Double area;

    @ApiModelProperty(value = "满炉数", position = 5)
    private Integer fullTemp;

    @ApiModelProperty(value = "占用格子数", position = 6)
    private Integer countSlot;

    @ApiModelProperty(value = "进度", position = 7)
    private Double percent;
}
