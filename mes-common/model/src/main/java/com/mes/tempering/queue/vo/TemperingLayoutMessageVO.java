package com.mes.tempering.queue.vo;

import com.mes.tempering.queue.TemperingGlassQueueInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 14:37
 * @Description:
 */
@ApiModel(description = "版图总览信息")
@Data
public class TemperingLayoutMessageVO {

    @ApiModelProperty(value = "工程号")
    private String engineerId;

    @ApiModelProperty(value = "钢化版图")
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "提示摆片")
    private List<TemperingGlassQueueInfo> promptList;

    @ApiModelProperty(value = "总页数")
    private Integer totalSize;

    @ApiModelProperty(value = "版图列表")
    private List<TemperingGlassQueueInfo> temperingGlassInfoList;
}
