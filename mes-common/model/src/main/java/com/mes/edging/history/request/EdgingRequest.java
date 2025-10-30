package com.mes.edging.history.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "磨边历史")
@Data
public class EdgingRequest {
    @ApiModelProperty("设备id")
    private Integer deviceId;
    @ApiModelProperty("线路")
    private Integer workLine;
    @ApiModelProperty("玻璃id")
    private String glassId;

}


