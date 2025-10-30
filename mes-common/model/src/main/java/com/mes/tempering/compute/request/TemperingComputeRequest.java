package com.mes.tempering.compute.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 9:43
 * @Description:
 */
@Data
public class TemperingComputeRequest implements Serializable {

    @ApiModelProperty("工程号:project_no")
    @JsonProperty("project_no")
    private String engineerId;

    @ApiModelProperty("厚度:glass_thickness")
    @JsonProperty("glass_thickness")
    private String thickness;
    @ApiModelProperty("订单序号:glass_type")
    @JsonProperty("glass_type")
    private String orderNumber;
    @ApiModelProperty("混排程度:chaos_pct")
    @JsonProperty("chaos_pct")
    private Double chaosPct;
    @ApiModelProperty("理片笼空闲量：cage_free")
    @JsonProperty("cage_free")
    private Double cageFree;
    @ApiModelProperty("钢化加热时间:tempering_time")
    @JsonProperty("tempering_time")
    private String temperingTime;
    @ApiModelProperty("模式：0最大片数 1最大面积:heat_mode")
    @JsonProperty("heat_mode")
    private Integer heatMode;
    @ApiModelProperty("最大装载率:max_load_pct")
    @JsonProperty("max_load_pct")
    private Double maxLoadPct;
    @ApiModelProperty("最大面积 钢化模式 0表示否:max_area")
    @JsonProperty("max_area")
    private Integer maxArea;
    @ApiModelProperty("最大片数：钢化模式 0表示否:max_qty")
    @JsonProperty("max_qty")
    private Integer maxQty;
    @ApiModelProperty("上片宽:load_width")
    @JsonProperty("load_width")
    private Integer loadWidth;
    @ApiModelProperty("上片高:load_length")
    @JsonProperty("load_length")
    private Integer loadLength;
    @ApiModelProperty("x轴间隔:x_space")
    @JsonProperty("x_space")
    private Integer xSpace;
    @ApiModelProperty("y轴间隔:y_space")
    @JsonProperty("y_space")
    private Integer ySpace;
    @ApiModelProperty("实际装载率:load_rate")
    @JsonProperty("load_rate")
    private Double loadRate;
    @ApiModelProperty("订单序号:furnaces_qty")
    @JsonProperty("furnaces_qty")
    private String furnacesQty;
    @ApiModelProperty("旋转模式:rotate_mode")
    @JsonProperty("rotate_mode")
    private Integer rotateMode;
    @ApiModelProperty("订单序号:polys_allow_rotate")
    @JsonProperty("polys_allow_rotate")
    private Integer polysAllowRotate;
    @ApiModelProperty(value = "流程卡数据:process_cards")
    @JsonProperty("process_cards")
    private List<ProcessCardsRequest> flowCardInfoList;

}