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
public class TemperingGlassDetailsVO implements Serializable {
    @ApiModelProperty(value = "流程卡:process_id")
    @JsonProperty("process_id")
    private String flowCardId;
    @ApiModelProperty(value = "层:technology_number")
    @JsonProperty("technology_number")
    private Integer layer;
    @ApiModelProperty(value = "钢化版图id:tempering_layout_id")
    @JsonProperty("tempering_layout_id")
    private Integer temperingLayoutId;
    @ApiModelProperty(value = "版序:tempering_feed_sequence")
    @JsonProperty("tempering_feed_sequence")
    private Integer temperingFeedSequence;
    @ApiModelProperty(value = "宽:width")
    @JsonProperty("width")
    private Double width;
    @ApiModelProperty(value = "高:height")
    @JsonProperty("height")
    private Double height;

    @ApiModelProperty(value = "X 坐标:x_coordinate")
    @JsonProperty("x_coordinate")
    private Integer xCoordinate;

    @ApiModelProperty(value = "Y 坐标")
    @JsonProperty("y_coordinate")
    private Integer yCoordinate;

    @ApiModelProperty(value = "是否旋转（角度，单位：度）")
    @JsonProperty("angle")
    private Integer angle;

    @ApiModelProperty(value = "订单序号:order_number")
    @JsonProperty("order_number")
    private Integer orderNumber;
    @ApiModelProperty(value = "总层数:layers_number")
    @JsonProperty("layers_number")
    private Integer totalLayer;

}
