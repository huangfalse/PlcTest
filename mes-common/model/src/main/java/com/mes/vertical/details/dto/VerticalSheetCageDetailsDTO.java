package com.mes.vertical.details.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 立式理片笼详情表(VerticalSheetCageDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:52:24
 */
@ApiModel(description = "可调度玻璃信息")
@Data
public class VerticalSheetCageDetailsDTO {


    @ApiModelProperty(value = "工程号 ID", position = 1)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图 ID", position = 2)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "流程卡号", position = 3)
    private Integer flowCardId;

    @ApiModelProperty(value = "层", position = 4)
    private Integer layer;

    @ApiModelProperty(value = "组号", position = 5)
    private Integer virtualSlot;

}

