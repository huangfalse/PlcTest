package com.mes.load.details;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 上片工位表(LoadStationDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:30:20
 */
@ApiModel(description = "上片工位表(LoadStationDetails)表实体类")
@Data
public class LoadStationDetails{

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("格子id")
    private Integer slot;

    @ApiModelProperty("原片宽")
    private Double rawWidth;

    @ApiModelProperty("原片高")
    private Double rawHeight;

    @ApiModelProperty("原片厚度")
    private Double rawThickness;

    @ApiModelProperty("膜系")
    private String filmsId;

    @ApiModelProperty("原片边距")
    private Integer rawMargin;

    @ApiModelProperty("生厂商")
    private String manufacturer;

    @ApiModelProperty("原片编号")
    private String rawNumber;

    @ApiModelProperty("开始数量")
    private Integer startQuantity;

    @ApiModelProperty("剩余数量")
    private Integer remainQuantity;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("架子号")
    private Integer shelf;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}

