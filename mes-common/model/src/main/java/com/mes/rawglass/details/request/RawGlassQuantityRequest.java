package com.mes.rawglass.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (RawGlassStorageDetails)表实体类
 *
 * @author zsh
 * @since 2025-06-12 11:47:20
 */
@ApiModel(description = "更新原片数量信息")
@Data
public class RawGlassQuantityRequest {
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "设备ID",position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "原片边距", position = 3)
    private Integer rawMargin;

    @ApiModelProperty(value = "原片宽", position = 4)
    private Double rawWidth;

    @ApiModelProperty(value = "原片高", position = 5)
    private Double rawHeight;

    @ApiModelProperty(value = "原片厚度", position = 6)
    private Double rawThickness;

    @ApiModelProperty(value = "膜系", position = 7)
    private String filmsId;

    @ApiModelProperty(value = "数量", position = 8)
    private Integer remainQuantity;

    @ApiModelProperty(value = "生厂商", position = 9)
    private String manufacturer;

    @ApiModelProperty(value = "原片编号", position = 10)
    private String rawNumber;

    @ApiModelProperty(value = "格子号", position = 11)
    private Integer slot;
}

