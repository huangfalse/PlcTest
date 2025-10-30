package com.mes.tempering.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 钢化参数配置表(TemperingParameter)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:00:03
 */
@ApiModel(description = "钢化参数配置表(TemperingParameter)表实体类")
@Data
public class TemperingParameterImportRequest {

    @ApiModelProperty(value = "工程号", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "是否钢化（0 否，1 是）", position = 3)
    private Integer heatMode;

    @ApiModelProperty(value = "计算模式（如排版方式、布局策略等）", position = 4)
    private Integer chaosPct;

    @ApiModelProperty(value = "混排程度（用于衡量玻璃混合排布的比例）", position = 5)
    private Integer maxLoadPct;

    @ApiModelProperty(value = "最大装载率（单位：%）", position = 6)
    private Double loadWidth;

    @ApiModelProperty(value = "装载区宽度（单位：mm）", position = 7)
    private Double loadLength;

    @ApiModelProperty(value = "装载区长度（单位：mm）", position = 8)
    @JsonProperty("xSpace")
    private Double xSpace;

    @ApiModelProperty(value = "X 方向间隔（单位：mm）", position = 9)
    @JsonProperty("ySpace")
    private Double ySpace;

    @ApiModelProperty(value = "Y 方向间隔（单位：mm）", position = 10)
    private Integer rotateMode;

    @ApiModelProperty(value = "旋转模式（如 0 禁止，1 允许）", position = 11)
    private Integer allowRotate;

    @ApiModelProperty(value = "是否允许横排（0 否，1 是）", position = 12)
    private Integer tempering;

    @ApiModelProperty(value = "幕墙模式（0 否，1 是）", position = 13)
    private Integer curtainWall;

}

