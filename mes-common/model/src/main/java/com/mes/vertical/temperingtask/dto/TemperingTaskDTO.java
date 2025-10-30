package com.mes.vertical.temperingtask.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/27 14:20
 * @Description:
 */
@ApiModel(description = "理片笼钢化相关")
@Data
public class TemperingTaskDTO {
    @ApiModelProperty(value = "工程号", position = 1)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id", position = 2)
    private Integer temperingLayoutId;

}
