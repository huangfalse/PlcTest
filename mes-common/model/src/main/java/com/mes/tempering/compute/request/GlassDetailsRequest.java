package com.mes.tempering.compute.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 10:13
 * @Description:
 */
@Data
public class GlassDetailsRequest implements Serializable {

    @ApiModelProperty(value = "流程卡:process_id")
    @JsonProperty("process_id")
    private String flowCardId;
    @ApiModelProperty(value = "层:technology_number")
    @JsonProperty("technology_number")
    private Integer layer;
    @ApiModelProperty(value = "订单序号:order_number")
    @JsonProperty("order_number")
    private Integer productSortNumber;
    @ApiModelProperty(value = "总层数:layers_number")
    @JsonProperty("layers_number")
    private Integer totalLayer;
    @ApiModelProperty(value = "配片宽:max_width")
    @JsonProperty("max_width")
    private Double maxWidth;
    @ApiModelProperty(value = "配片高:max_height")
    @JsonProperty("max_height")
    private Double maxHeight;
    @ApiModelProperty(value = "宽:child_width")
    @JsonProperty("child_width")
    private Double width;
    @ApiModelProperty(value = "高:child_height")
    @JsonProperty("child_height")
    private Double height;
    @ApiModelProperty(value = "数量:quantity")
    @JsonProperty("quantity")
    private Integer quantity;
    @ApiModelProperty(value = "补片状态:patch_state")
    @JsonProperty("patch_state")
    private Integer patchState;

}