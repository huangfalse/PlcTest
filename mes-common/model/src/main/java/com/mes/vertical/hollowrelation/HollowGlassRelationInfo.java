package com.mes.vertical.hollowrelation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 中空理片笼关系表(HollowGlassRelationInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:55:07
 */
@ApiModel(description = "中空理片笼关系表(HollowGlassRelationInfo)表实体类")
@Data
public class HollowGlassRelationInfo {

    @ApiModelProperty(value = "中空玻璃关系表主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程号（init）", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "炉号", position = 3)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "版图序号（用于排序）", position = 4)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "玻璃 ID", position = 5)
    private String glassId;

    @ApiModelProperty(value = "流程卡编号（init）", position = 6)
    private String flowCardId;

    @ApiModelProperty(value = "宽度（单位：mm，init）", position = 7)
    private Double width;

    @ApiModelProperty(value = "高度（单位：mm，init）", position = 8)
    private Double height;

    @ApiModelProperty(value = "总层数（init）", position = 9)
    private Integer totalLayer;

    @ApiModelProperty(value = "当前所在层数（init）", position = 10)
    private Integer layer;

    @ApiModelProperty(value = "组号（init）", position = 11)
    private Integer virtualSlot;

    @ApiModelProperty(value = "组内序号（init）", position = 12)
    private Integer slotSequence;

    @ApiModelProperty(value = "出片总序号（init）", position = 13)
    private Integer hollowSequence;

    @ApiModelProperty(value = "订单排序序号（init）", position = 14)
    private Integer orderSort;

    @ApiModelProperty(value = "膜系 ID", position = 15)
    private String filmsId;

    @ApiModelProperty(value = "厚度（单位：mm）", position = 16)
    private Double thickness;

    @ApiModelProperty(value = "状态值（根据业务状态定义）", position = 17)
    private Integer state;

}

