package com.mes.rawglass.details.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/7 9:57
 * @Description:
 */
@ApiModel(description = "原片信息")
@Data
public class RawLoadDetailsVO {

    @ApiModelProperty(value = "原片宽", position = 1)
    private Double rawWidth;

    @ApiModelProperty(value = "原片高", position = 2)
    private Double rawHeight;

    @ApiModelProperty(value = "原片厚度", position = 3)
    private Double rawThickness;

    @ApiModelProperty(value = "膜系", position = 4)
    private String filmsId;

    @ApiModelProperty(value = "原片边距", position = 5)
    private Integer rawMargin;

    @ApiModelProperty(value = "生厂商", position = 6)
    private String manufacturer;

    @ApiModelProperty(value = "原片编号", position = 7)
    private String rawNumber;

    @ApiModelProperty(value = "剩余数量", position = 8)
    private Integer remainQuantity;

}
