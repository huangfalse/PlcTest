package com.mes.vertical.temperingtask.request;

import com.mes.base.request.TimeRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/27 14:20
 * @Description:
 */
@ApiModel(description = "理片笼钢化相关")
@Data
public class TemperingTaskRequest extends TimeRequest {

    @ApiModelProperty(value = "任务id", position = 1)
    private Long id;

    @ApiModelProperty(value = "设备号", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "工程号", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id:为空 默认-1 指定工程调用，非空0 人工下片  其他指定钢化", position = 3)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "是否强制出片", position = 4)
    private Integer isForce;

    @ApiModelProperty(value = "状态", position = 5)
    private Integer state;

    @ApiModelProperty(value = "目标", position = 6)
    private Integer target;

    @ApiModelProperty(value = "任务id集合", position = 7)
    private List<Long> idList;

}
