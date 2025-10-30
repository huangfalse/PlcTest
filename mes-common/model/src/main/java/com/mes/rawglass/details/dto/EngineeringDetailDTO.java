package com.mes.rawglass.details.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huang
 * @since 2025/8/7
 */
@ApiModel(description = "工程详情")
@Data
public class EngineeringDetailDTO {
    @ApiModelProperty("工程号")
    private String engineeringId;

    @ApiModelProperty("膜系")
    private String filmsId;

    @ApiModelProperty("宽")
    private Double rawGlassWidth;

    @ApiModelProperty("高")
    private Double rawGlassHeight;

    @ApiModelProperty("厚度")
    private Double rawGlassThickness;

    @ApiModelProperty("需求数量")
    private Integer totalRequired;

    @ApiModelProperty("库内数量")
    private Integer totalStock;

    @ApiModelProperty("短缺数量")
    private Integer shortageQuantity;
}
