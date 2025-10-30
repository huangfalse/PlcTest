package com.mes.rawglass.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 上片工位表(LoadStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:32
 */
@ApiModel(description = "上片任务类型请求")
@Data
public class LoadGlassHistoryTaskRequest extends PageRequest {
    /**
     * 指定前端传递的日期格式
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 指定前端传递的日期格式
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("需要查询的任务状态集合 0默认未执行任务 1执行中 2结束任务")
    private List<Integer> taskState;

}

