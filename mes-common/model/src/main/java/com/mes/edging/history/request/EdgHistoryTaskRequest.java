package com.mes.edging.history.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : zhoush
 * @Date: 2025/7/21 15:56
 * @Description:
 */
@ApiModel(description = "磨边历史查询请求")
@Data
public class EdgHistoryTaskRequest extends PageRequest implements Serializable {

    @ApiModelProperty(value = "玻璃id")
    private String glassId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("线路")
    private Integer workLine;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
