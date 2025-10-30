package com.mes.vertical.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/10 16:40
 * @Description:
 */
@Data
public class VerticalSheetCageHistoryRequest extends PageRequest {

    /**
     * 玻璃id
     */
    @ApiModelProperty(value = "玻璃id", position = 1)
    private String glassId;
    /**
     * 起始格子
     */
    @ApiModelProperty(value = "起始格子", position = 2)
    private Integer startSlot;
    /**
     * 目标格子
     */
    @ApiModelProperty(value = "目标格子", position = 3)
    private Integer targetSlot;
    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态 0 开始 2完成 3破损 4未上车", position = 4)
    private List<Integer> taskStateList;
    /**
     * 任务类型
     */
    @ApiModelProperty(value = "任务类型 1进片 2出片 3调度", position = 5)
    private List<Integer> taskTypeList;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", position = 6)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", position = 7)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}

