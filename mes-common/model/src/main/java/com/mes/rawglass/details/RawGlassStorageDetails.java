package com.mes.rawglass.details;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * (RawGlassStorageDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:15:30
 */
@ApiModel(description = "(RawGlassStorageDetails)表实体类")
@Data
public class RawGlassStorageDetails {

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备id", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "工位id", position = 3)
    private Integer slot;

    @JSONField(name = "width")
    @ApiModelProperty(value = "原片宽", position = 4)
    private Double rawWidth;

    @JSONField(name = "height")
    @ApiModelProperty(value = "原片高", position = 5)
    private Double rawHeight;

    @JSONField(name = "thickness")
    @ApiModelProperty(value = "原片厚度", position = 6)
    private Double rawThickness;

    @JSONField(name = "model")
    @ApiModelProperty(value = "膜系", position = 7)
    private String filmsId;

    @ApiModelProperty(value = "原片边距", position = 8)
    private Integer rawMargin;

    @JSONField(name = "name")
    @ApiModelProperty(value = "生厂商", position = 9)
    private String manufacturer;

    @JSONField(name = "materialCode")
    @ApiModelProperty(value = "原片编号", position = 10)
    private String rawNumber;

    @ApiModelProperty(value = "开始数量", position = 11)
    private Integer startQuantity;

    @JSONField(name = "quantity")
    @ApiModelProperty(value = "剩余数量", position = 12)
    private Integer remainQuantity;

    @ApiModelProperty(value = "状态", position = 13)
    private Integer state;

    @ApiModelProperty(value = "架子号", position = 14)
    private Integer shelf;

    @ApiModelProperty(value = "创建时间", position = 15)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 16)
    private Date updateTime;

}

