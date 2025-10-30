package com.mes.rawglass.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/7 9:58
 * @Description:
 */
@ApiModel(description = "原片上片请求")
@Data
public class RawLoadRequest {

    @ApiModelProperty(value = "原片宽", position = 1)
    private Double rawWidth;

    @ApiModelProperty(value = "原片高", position = 2)
    private Double rawHeight;

    @ApiModelProperty(value = "原片厚度", position = 3)
    private Double rawThickness;

    @ApiModelProperty(value = "膜系", position = 4)
    private String filmsId;

    @ApiModelProperty(value = "生厂商", position = 5)
    private String manufacturer;

    @ApiModelProperty(value = "原片编号", position = 6)
    private String rawNumber;

    @ApiModelProperty(value = "原片数量", position = 7)
    private Integer rawQuantity;

    @ApiModelProperty(value = "工位号", position = 8)
    private Integer slot;

    @ApiModelProperty(value = "架子号", position = 9)
    private Integer shelf;

    @ApiModelProperty(value = "剩余数量", position = 10)
    private Integer remainQuantity;

}
