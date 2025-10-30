package com.mes.vertical.history.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/11/30 13:34
 * @Description:
 */
@Data
public class VerticalSheetCarTaskDTO {

    @ApiModelProperty(value = "玻璃 ID", position = 1)
    private String glassId;

    @ApiModelProperty(value = "起始格子编号", position = 2)
    private Integer startSlot;

    @ApiModelProperty(value = "目标格子编号", position = 3)
    private Integer targetSlot;

}
