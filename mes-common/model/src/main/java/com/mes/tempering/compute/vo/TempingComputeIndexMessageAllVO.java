package com.mes.tempering.compute.vo;

import com.mes.tempering.compute.TemperingComputeRecord;
import com.mes.vertical.temperingtask.vo.TemperingTaskVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/25 19:24
 * @Description:
 */
@ApiModel(description = "模拟计算任务数据")
@Data
public class TempingComputeIndexMessageAllVO {

    @ApiModelProperty(value = "模拟计算总任务")
    private TemperingComputeRecord record;

    @ApiModelProperty(value = "当前炉")
    private Integer currentTempering;

    @ApiModelProperty(value = "版图总览信息")
    private List<TemperingTaskVO> temperingTaskList;

    @ApiModelProperty(value = "钢化开关")
    private Boolean temperingSwitch;

}
