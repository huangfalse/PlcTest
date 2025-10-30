package com.mes.tempering.queue.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 14:37
 * @Description:
 */
@ApiModel(description = "钢化界面各阶段版图信息")
@Data
public class TempIndexMessageAllVO {

    @ApiModelProperty(value = "炉数")
    private Integer totalSize;


    @ApiModelProperty(value = "版图总览信息")
    private List<TemperingLayoutMessageVO> tempIndexMessageVOList;
}
