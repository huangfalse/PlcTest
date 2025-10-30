package com.mes.horizontal.details;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 卧理理片笼详情表(HorizontalSheetCageDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 11:12:42
 */
@ApiModel(description = "卧理理片笼详情表(HorizontalSheetCageDetails)表实体类")
@Data
public class HorizontalSheetCageDetails {

    @ApiModelProperty("卧式理片笼详情表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("栅格号")
    private Integer slot;

    @ApiModelProperty("工程id")
    private String engineerId;

    @ApiModelProperty("玻璃id")
    private String glassId;

    @ApiModelProperty("流程卡号")
    private String flowCardId;

    @ApiModelProperty("玻璃类型")
    private Integer glassType;

    @ApiModelProperty("宽")
    private Double width;

    @ApiModelProperty("高")
    private Double height;

    @ApiModelProperty("厚度")
    private Double thickness;

    @ApiModelProperty("膜系")
    private String filmsId;

    @ApiModelProperty("钢化版图id")
    private Integer temperingLayoutId;

    @ApiModelProperty("钢化版图片序")
    private Integer temperingFeedSequence;

    @ApiModelProperty("玻璃间隙")
    private Integer gap;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

//    @ApiModelProperty("格子状态")
//    private transient  Integer enableState;

}

