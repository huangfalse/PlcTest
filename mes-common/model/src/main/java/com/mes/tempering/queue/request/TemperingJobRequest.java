package com.mes.tempering.queue.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 钢化队列表(TemperingGlassQueueInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:58:49
 */
@ApiModel(description = "钢化定时任务相关参数")
@Data
public class TemperingJobRequest {

    @ApiModelProperty(value = "设备id", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "队列id", position = 2)
    private Long id;

    @ApiModelProperty(value = "状态", position = 3)
    private Integer state;

    @ApiModelProperty(value = "工程号", position = 4)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id", position = 5)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "玻璃id", position = 6)
    private String glassId;

    @ApiModelProperty(value = "钢化阶段：1钢化前 2钢化中 3钢化后", position = 7)
    private Integer temperingStage;

}

