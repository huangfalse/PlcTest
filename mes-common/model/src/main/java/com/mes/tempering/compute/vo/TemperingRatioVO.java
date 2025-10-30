package com.mes.tempering.compute.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 11:02
 * @Description:
 */
@Data
public class TemperingRatioVO implements Serializable {

    @ApiModelProperty(value = "面积:area")
    @JsonProperty("area")
    private Double area;
    @ApiModelProperty(value = "数量:glass_total")
    @JsonProperty("glass_total")
    private Integer glassTotal;

    @ApiModelProperty(value = "装载率:ratio")
    @JsonProperty("ratio")
    private Double ratio;

    @ApiModelProperty(value = "是否合并:tempering_layout_id ")
    @JsonProperty("tempering_layout_id")
    private Integer temperingLayoutId;

}
