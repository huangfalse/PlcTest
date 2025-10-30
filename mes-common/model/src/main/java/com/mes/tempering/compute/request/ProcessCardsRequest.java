package com.mes.tempering.compute.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 10:12
 * @Description:
 */
@Data
public class ProcessCardsRequest implements Serializable {

    @ApiModelProperty(value = "层:glass_details")
    @JsonProperty("glass_details")
    List<GlassDetailsRequest> glassDetails;
    @ApiModelProperty(value = "流程卡号:process_no")
    @JsonProperty("process_no")
    private String flowCardId;
    @ApiModelProperty(value = "层:layers")
    @JsonProperty("layers")
    private Integer layers;
    @ApiModelProperty(value = "总层:total_layers")
    @JsonProperty("total_layers")
    private Integer totalLayer;
    @ApiModelProperty(value = "小片总数:total_num")
    @JsonProperty("total_num")
    private Integer totalNum;
    @ApiModelProperty(value = "小片总面积:total_area")
    @JsonProperty("total_area")
    private Double totalArea;
    @ApiModelProperty(value = "是否必选:is_must")
    @JsonProperty("is_must")
    private boolean isMust;
    @ApiModelProperty(value = "是否旋转:allow_rotate")
    @JsonProperty("allow_rotate")
    private boolean allowRotate;
    @ApiModelProperty(value = "优先级:priority_level")
    @JsonProperty("priority_level")
    private Integer priorityLevel;
    @ApiModelProperty(value = "是否钢化:tempering")
    @JsonProperty("tempering")
    private boolean tempering;
    @ApiModelProperty(value = "幕墙模式:curtain_wall")
    @JsonProperty("curtain_wall")
    private boolean curtainWall;
    @ApiModelProperty(value = "补片状态:patch_state")
    @JsonProperty("patch_state")
    private Integer patchState;
    @ApiModelProperty(value = "是否合并:merge")
    @JsonProperty("merge")
    private Integer merge;
}
