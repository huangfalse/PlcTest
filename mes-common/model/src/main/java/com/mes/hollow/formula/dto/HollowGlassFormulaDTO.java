package com.mes.hollow.formula.dto;

import com.mes.hollow.formula.HollowFormulaDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/30 15:34
 * @Description:
 */
@ApiModel(description = "中空配方信息及玻璃信息用于发送除膜数据")
@Data
public class HollowGlassFormulaDTO extends HollowFormulaDetails {

    @ApiModelProperty(value = "宽度", position = 1)
    private Double width;

    @ApiModelProperty(value = "高度", position = 2)
    private Double height;

    @ApiModelProperty(value = "厚度", position = 3)
    private Double thickness;
}

