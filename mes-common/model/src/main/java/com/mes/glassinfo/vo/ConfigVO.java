package com.mes.glassinfo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 11:12
 * @Description:
 */

@ApiModel(description = "配置实体类")
@Data
public class ConfigVO implements Serializable {


    @ApiModelProperty(value = "笼子配置", position = 1)
    private List<Integer> cage;

    @ApiModelProperty(value = "产线配置", position = 2)
    private List<WorkLineConfigVO> workLineConfigVOS;

    @ApiModelProperty(value = "钢化 配置", position = 3)
    private List<Integer> temp;

    @ApiModelProperty(value = "笼子厚度", position = 4)
    private List<String> cageThickness;

}
