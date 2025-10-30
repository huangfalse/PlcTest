package com.mes.cutting.history.vo;

import com.mes.cutting.history.CutHistoryTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/8/13 16:29
 * @Description:
 */
@Data
public class CuttingIndexMessageVO {

    @ApiModelProperty(value = "原片版图小片信息", position = 1)
    private List<CutTerritoryVO> cutTerritoryVOList;

    @ApiModelProperty(value = "原片工程号", position = 2)
    private String engineerId;


    @ApiModelProperty(value = "原片片序", position = 3)
    private Integer rawSequence;

    @ApiModelProperty(value = "总片序", position = 4)
    private Integer totalRawSequence;

    @ApiModelProperty(value = "宽", position = 5)
    private Double rawGlassWidth;

    @ApiModelProperty(value = "高", position = 6)
    private Double rawGlassHeight;

    @ApiModelProperty(value = "联机状态", position = 7)
    private String onlineState;

    @ApiModelProperty(value = "最近三条切割任务", position = 8)
    private List<CutHistoryTask> cutHistoryTaskList;

}
