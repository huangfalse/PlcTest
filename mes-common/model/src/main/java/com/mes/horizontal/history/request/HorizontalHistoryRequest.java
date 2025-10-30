package com.mes.horizontal.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "磨边历史")
@Data
public class HorizontalHistoryRequest extends PageRequest {

    @ApiModelProperty("玻璃id")
    private String glassId;
    @ApiModelProperty("设备id")
    private Integer deviceId;
    @ApiModelProperty("线路")
    private Integer workLine;
    @ApiModelProperty("任务类型")
    private Integer taskType;
    @ApiModelProperty("任务状态 0开始任务 1结束任务")
    private Integer taskState;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}


