package com.mes.glassinfo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 11:12
 * @Description:
 */

@ApiModel(description = "产线配置实体类")
@Data
public class WorkLineConfigVO implements Serializable {


    @ApiModelProperty(value = "产品类型", position = 1)
    private String productType;

    @ApiModelProperty(value = "所占笼子", position = 2)
    private List<Integer> workCage;

    @ApiModelProperty(value = "剩余存放面积", position = 3)
    private Double RemainArea;

    @ApiModelProperty(value = "剩余存放面积", position = 4)
    private Integer RemainSlot;

    @ApiModelProperty(value = "百分比", position = 5)
    private Double percent;

    @ApiModelProperty(value = "线路", position = 6)
    private List<Boolean> workLine;

    @ApiModelProperty(value = "笼子厚度配置", position = 7)
    private List<String> cageThicknessConfig;

    @ApiModelProperty(value = "各厚度面积占比", position = 8)
    private List<Map<String, Double>> thicknessArea;

}
