package com.mes.hollow.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/3/11 14:15
 * @Description:
 */
@ApiModel(description = "中空历史任务请求")
@Data
public class HollowHistoryTaskRequest extends PageRequest {

    @ApiModelProperty(value = "线路按照实际界面默认 路线：930（李赛克，界面需要输入相关数据） 931")
    private Integer cell;

    @ApiModelProperty(value = "流程卡")
    private String flowCardId;

    @ApiModelProperty(value = "是否强制 0非强制 1强制")
    private List<Integer>isForceList;

    @ApiModelProperty(value = "状态0新增 1执行中 2暂停 3结束")
    private List<Integer> stateList;

    @ApiModelProperty(value = "开始时间", position = 6)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间", position = 7)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}
