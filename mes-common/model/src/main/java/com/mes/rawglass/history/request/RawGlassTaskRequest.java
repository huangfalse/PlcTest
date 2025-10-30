package com.mes.rawglass.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author huang
 * @since 2025/7/9
 */
@ApiModel(description = "")
@Data
public class RawGlassTaskRequest extends PageRequest {

    @ApiModelProperty(value = "任务类型   1 入库    2 出库    3 调度", position = 1)
    private List<Integer> taskType;

    @ApiModelProperty(value = "任务状态   1 新建  2 执行中   3 完成", position = 2)
    private List<Integer> taskState;

    @ApiModelProperty(value = "开始时间", position = 3)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间", position = 4)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}
