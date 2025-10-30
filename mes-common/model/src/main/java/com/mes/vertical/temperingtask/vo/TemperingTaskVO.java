package com.mes.vertical.temperingtask.vo;

import com.mes.vertical.temperingtask.TemperingTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/25 19:27
 * @Description:
 */
@ApiModel(description = "钢化任务界面显示")
@Data
public class TemperingTaskVO extends TemperingTask {


    @ApiModelProperty(value = "笼内数量")
    private Integer realCount;

    @ApiModelProperty(value = "厚度")
    private Double thickness;

    @ApiModelProperty(value = "膜系")
    private String filmsId;
}
