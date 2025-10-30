package com.mes.rawglass.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * (RawGlassStorageDetails)表实体类
 *
 * @author zsh
 * @since 2025-06-12 11:47:20
 */
@ApiModel(description = "新增原片信息")
@Data
public class RawGlassDetailsRequest {

    @NotNull
    @ApiModelProperty(value = "详情id")
    private Long id;
    @NotNull
    @ApiModelProperty(value = "设备id", position = 2)
    private Integer deviceId;

    @NotNull
    @ApiModelProperty(value = "格子id", position = 3)
    private Integer slot;

    @NotNull
    @ApiModelProperty(value = "架子号：分为单双数", position = 4)
    private Integer shelf;

    @NotNull
    @ApiModelProperty(value = "原片宽", position = 5)
    private Double rawWidth;

    @NotNull
    @ApiModelProperty(value = "原片高", position = 6)
    private Double rawHeight;

    @NotNull
    @ApiModelProperty(value = "原片厚度", position = 7)
    private Double rawThickness;

    @NotNull
    @ApiModelProperty(value = "膜系", position = 8)
    private String filmsId;

    @NotNull
    @ApiModelProperty(value = "原片边距", position = 9)
    private Integer rawMargin;

    @ApiModelProperty(value = "生厂商", position = 10)
    private String manufacturer;

    @ApiModelProperty(value = "原片编号", position = 11)
    private String rawNumber;

    @NotNull
    @ApiModelProperty(value = "剩余数量", position = 12)
    private Integer remainQuantity;

    @ApiModelProperty(value = "设备id列表", position = 13)
    private List<Integer> deviceIdList;


}

