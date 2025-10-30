package com.mes.base.bigstorage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/18 15:04
 * @Description:
 */
@Data
public class BigStorageCageBaseInfo {
    /**
     * 工程号
     */
    @ApiModelProperty(value = "工程号")
    private String engineerId;
    /**
     * 玻璃id
     */
    @ApiModelProperty(value = "玻璃id")
    private String glassId;
    /**
     * 钢化版图id
     */
    @ApiModelProperty(value = "钢化版图id")
    private Integer temperingLayoutId;
    /**
     * 钢化版图id
     */
    @ApiModelProperty(value = "组号")
    private Integer virtualSlot;

    /**
     * 钢化版图片序
     */
    @ApiModelProperty(value = "钢化版图片序")
    private Integer temperingFeedSequence;

    /**
     * 宽
     */
    @ApiModelProperty(value = "宽")
    private double width;
    /**
     * 宽
     */
    @ApiModelProperty(value = "高")
    private double height;

    /**
     * 厚度
     */
    @ApiModelProperty(value = "厚度")
    private double thickness;
    /**
     * 膜系
     */
    @ApiModelProperty(value = "膜系")
    private String filmsId;

    /**
     * 格子号
     */
    @ApiModelProperty(value = "格子号")
    private Integer slot;
}
