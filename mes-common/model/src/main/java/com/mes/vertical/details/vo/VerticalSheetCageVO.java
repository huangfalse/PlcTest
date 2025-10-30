package com.mes.vertical.details.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/30 13:26
 * @Description:
 */
@Data
public class VerticalSheetCageVO {

    @ApiModelProperty(value = "笼子号")
    private Integer deviceId;
    @ApiModelProperty(value = "格子号")
    private Integer slot;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "使用率")
    private Integer percentage;
}
