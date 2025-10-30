package com.mes.rawglass.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huang
 * @since 2025/6/25
 */

@ApiModel(description = "")
@Data
public class DetailsRequest {

    @ApiModelProperty(value = "原片宽",position = 1)
    private Double rawWidth;

    @ApiModelProperty(value = "原片高", position = 2)
    private Double rawHeight;

    @ApiModelProperty(value = "原片厚度", position = 3)
    private Double rawThickness;

    @ApiModelProperty(value = "膜系", position = 4)
    private String filmsId;

    @ApiModelProperty(value = "原片边距", position = 5)
    private Integer rawMargin;

    @ApiModelProperty(value = "剩余数量", position = 6)
    private Integer remainQuantity;

    @ApiModelProperty(value = "栅格号", position = 7)
    private Integer slot;

    @ApiModelProperty(value = "设备id", position = 8)
    private Integer deviceId;

    @ApiModelProperty(value = "吊装位", position = 9)
    private Integer leftingStation;
}
