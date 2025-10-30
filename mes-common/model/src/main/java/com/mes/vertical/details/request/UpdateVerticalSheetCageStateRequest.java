package com.mes.vertical.details.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/5 9:59
 * @Description:
 */
@Data
public class UpdateVerticalSheetCageStateRequest {

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "玻璃id", position = 2)
    private String glassId;

    @ApiModelProperty(value = "玻璃状态：8破损 9拿走 101删除  102人工下片 ", position = 3)
    private int state;

}
