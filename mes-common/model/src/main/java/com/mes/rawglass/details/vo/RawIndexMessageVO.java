package com.mes.rawglass.details.vo;

import com.mes.rawglass.history.RawGlassStorageHistoryTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/16 20:30
 * @Description:
 */
@ApiModel(description = "前端主界面所有数据")
@Data
public class RawIndexMessageVO {

    @ApiModelProperty(value = "仓储历史任务")
    List<RawGlassStorageHistoryTask> historyTaskList;

    @ApiModelProperty(value = "仓储架子数据")
    List<RawGlassCountVO> rawGlassCountVOList;

    @ApiModelProperty(value = "仓储任务开关")
    Boolean rawStorageSwitch;
}
