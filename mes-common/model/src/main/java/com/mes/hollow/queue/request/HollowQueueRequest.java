package com.mes.hollow.queue.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "中空队列接口参数")
@Data
public class HollowQueueRequest {

    @ApiModelProperty(value = "线路")
    private Integer cell;

    @ApiModelProperty(value = "层")
    private Integer layer;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "关系id")
    private Integer relationId;

    @ApiModelProperty(value = "配方id")
    private Integer hollowSequence;

    @ApiModelProperty(value = "任务id")
    private Integer taskId;

}


