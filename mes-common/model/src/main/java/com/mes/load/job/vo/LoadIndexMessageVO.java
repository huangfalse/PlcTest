package com.mes.load.job.vo;

import com.mes.engineering.Engineering;
import com.mes.load.details.LoadStationDetails;
import com.mes.rawqueue.EngineeringRawQueue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 14:37
 * @Description:
 */
@ApiModel(description = "上片界面展示信息")
@Data
public class LoadIndexMessageVO {

    @ApiModelProperty(value = "设备状态", position = 1)
    private Integer onlineState;

    @ApiModelProperty(value = "任务请求", position = 1)
    private Integer plcRequest;

    @ApiModelProperty(value = "当前工程信息", position = 2)
    private Engineering engineering;

    @ApiModelProperty(value = "未领取的工程号Id", position = 3)
    private List<String> engineeringIdList;

    @ApiModelProperty(value = "工程原片队列", position = 4)
    private List<EngineeringRawQueue> engineeringRawQueueList;

    @ApiModelProperty(value = "上片位详情", position = 5)
    private List<LoadStationDetails> loadStationDetailsList;
}
