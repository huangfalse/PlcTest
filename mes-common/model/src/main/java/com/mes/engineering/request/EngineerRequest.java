package com.mes.engineering.request;

import com.mes.rawqueue.EngineeringRawQueue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 14:24
 * @Description:
 */
@Data
@ApiModel(description = "上片工程号信息")
public class EngineerRequest {

    @ApiModelProperty(value = "工程id")
    private String engineerId;

    @ApiModelProperty(value = "工程状态：1：开始上片 2：暂停上片 3：结束上片")
    private int state;

    @ApiModelProperty(value = "上片线路：1线 2线")
    private int stationCell;

    @ApiModelProperty(value = "工程下的原片队列")
    private List<EngineeringRawQueue> queueList;
}