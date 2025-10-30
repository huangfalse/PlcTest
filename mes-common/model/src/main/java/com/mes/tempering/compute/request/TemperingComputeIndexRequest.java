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
public class TemperingComputeIndexRequest implements Serializable {

    @ApiModelProperty("界面勾选流程卡相关")
    List<TemperingFlowCardIdRequest> temperingFlowCardIdRequestList;
    @ApiModelProperty("混排程度:chaos_pct")
    @JsonProperty("chaos_pct")
    private Double chaosPct;
    @ApiModelProperty("理片笼空闲量：cage_free")
    private Double cageFree;
    @ApiModelProperty("最大装载率:max_load_pct")
    private Double maxLoadPct;
    @ApiModelProperty("上片宽:load_width")
    private Integer loadWidth;
    @ApiModelProperty("上片高:load_length")
    private Integer loadLength;
    @ApiModelProperty("x轴间隔:x_space")
    private Integer xSpace;
    @ApiModelProperty("y轴间隔:y_space")
    private Integer ySpace;
    @ApiModelProperty("是否允许缺失")
    private Boolean allowMissing;
    @ApiModelProperty("模式：0最大片数 1最大面积:heat_mode")
    @JsonProperty("heat_mode")
    private Integer heatMode;
    @ApiModelProperty("最大面积 钢化模式 0表示否:max_area")
    @JsonProperty("max_area")
    private Integer maxArea;
    @ApiModelProperty("最大片数：钢化模式 0表示否:max_qty")
    @JsonProperty("max_qty")
    private Integer maxQty;
    @ApiModelProperty("玻璃厚度（单位：毫米）")
    private Double thickness;
    @ApiModelProperty("膜系编号")
    private String filmsId;


}