package com.mes.vertical.details.vo;

import com.mes.vertical.history.VerticalSheetCageHistoryTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author : zhoush
 * @Date: 2025/9/4 17:28
 * @Description:
 */
@Data
public class VerticalSheetCageIndexMessageVO {
    @ApiModelProperty(value = "笼子使用率")
    List<VerticalSheetCageVO> bigStorageCageUsage;
    @ApiModelProperty(value = "笼内数据汇总")
    List<VerticalSheetCageSummaryVO> bigStorageSummary;
    @ApiModelProperty(value = "进片任务")
    List<VerticalSheetCageHistoryTask> inTaskList;
    @ApiModelProperty(value = "出片任务")
    List<VerticalSheetCageHistoryTask> outTaskList;
    @ApiModelProperty(value = "设备下的所有笼子玻璃数量")
    private Map<Integer, List<VerticalSheetCageVO>> verticalSheetCageInfoMap;
    @ApiModelProperty(value = "钢化开关")
    private Boolean temperingSwitch;
    @ApiModelProperty(value = "调度开关")
    private Boolean dispatchSwitch;
    @ApiModelProperty(value = "进片联机状态")
    private Boolean inOnlineState;
    @ApiModelProperty(value = "出片联机状态")
    private Boolean outOnlineState;
    @ApiModelProperty(value = "进片请求")
    private Integer inPlcRequest;
    @ApiModelProperty(value = "出片请求")
    private Integer outPlcRequest;
    @ApiModelProperty(value = "进片启动")
    private Integer inMesSend;
    @ApiModelProperty(value = "出片启动")
    private Integer outMesSend;
}