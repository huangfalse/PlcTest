package com.mes.horizontal.station.vo;

import com.mes.horizontal.history.vo.HorizontalHistoryVO;
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
public class HorizontalIndexMessageVO {

    @ApiModelProperty(value = "笼子详情")
    List<HorizontalSheetCageVO> cageDetails;

    @ApiModelProperty(value = "当前执行任务")
    List<HorizontalHistoryVO> currentTask;
}
