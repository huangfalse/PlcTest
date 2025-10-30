package com.mes.vertical.details.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author : zhoush
 * @Date: 2025/9/4 17:04
 * @Description:
 */
@Data
public class VerticalSheetCageDetailsRequest extends PageRequest {

    @ApiModelProperty(value = "设备id", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "格子号", position = 2)
    private Integer slot;

    @ApiModelProperty(value = "流程卡", position = 3)
    private String flowCardId;

    @ApiModelProperty(value = "膜系", position = 4)
    private String filmsId;

    @ApiModelProperty(value = "厚度", position = 5)
    private int thickness;

    @ApiModelProperty(value = "钢化版图id", position = 6)
    private String engineerId;

    @ApiModelProperty(value = "玻璃id", position = 7)
    private String glassId;

    @ApiModelProperty(value = "玻璃报工状态：8破损 9拿走  调用更新状态/破损拿走接口必填", position = 8)
    private String state;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
