package com.mes.cutting.history.requst;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author : zhoush
 * @Date: 2025/8/12 15:12
 * @Description:
 */
@ApiModel(description = "版图识别入参")
@Data
public class CutTerritoryRequest {

    @ApiModelProperty(value = "原片工程号", position = 1)
    @NotNull
    private String engineerId;

    @ApiModelProperty(value = "原片片序", position = 2)
    @NotNull
    private int rawSequence;

    @ApiModelProperty(value = "线路", position = 3)
    private Integer workLine;

    @ApiModelProperty(value = "是否仅获取未磨边的 1：是  0：否，默认为0", position = 4)
    @NotNull
    private int isAll;

    @ApiModelProperty(value = "当前线路联机状态 默认0", position = 3)
    private String onlineState;
}
