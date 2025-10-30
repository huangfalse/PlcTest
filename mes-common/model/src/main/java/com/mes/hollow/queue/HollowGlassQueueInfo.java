package com.mes.hollow.queue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 中空队列表(HollowGlassQueueInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:01:51
 */
@ApiModel(description = "中空队列表(HollowGlassQueueInfo)表实体类")
@Data
public class HollowGlassQueueInfo {

    @ApiModelProperty(value = "大理片笼详情表主键 ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "设备 ID（所属设备编号）", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "虚拟栅格号（逻辑格位编号）", position = 3)
    private Integer virtualSlot;

    @ApiModelProperty(value = "实际栅格号（物理格位编号）", position = 4)
    private Integer slot;

    @ApiModelProperty(value = "玻璃 ID", position = 5)
    private String glassId;

    @ApiModelProperty(value = "小片在格子内的顺序", position = 6)
    private Integer sequence;

    @ApiModelProperty(value = "流程卡号", position = 7)
    private String flowCardId;

    @ApiModelProperty(value = "玻璃类型（如白玻、LOW-E 等）", position = 8)
    private Integer glassType;

    @ApiModelProperty(value = "玻璃宽度（单位：mm）", position = 9)
    private Double width;

    @ApiModelProperty(value = "玻璃高度（单位：mm）", position = 10)
    private Double height;

    @ApiModelProperty(value = "玻璃厚度（单位：mm）", position = 11)
    private Double thickness;

    @ApiModelProperty(value = "钢化版图 ID", position = 12)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图中图片的序号", position = 13)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "当前状态", position = 14)
    private Integer state;

    @ApiModelProperty(value = "工程号", position = 15)
    private String engineerId;

    @ApiModelProperty(value = "总层数（如双层、三层中空）", position = 16)
    private Integer totalLayer;

    @ApiModelProperty(value = "当前玻璃层号（第几层）", position = 17)
    private Integer layer;

    @ApiModelProperty(value = "中空顺序（出片或配对顺序）", position = 18)
    private Integer hollowSequence;

    @ApiModelProperty(value = "所属线路编号", position = 19)
    private Integer cell;

    @ApiModelProperty(value = "是否已配对（0 - 否，1 - 是）", position = 20)
    private Integer isPair;

    @ApiModelProperty(value = "膜系 ID（如 LOW-E 类型）", position = 21)
    private String filmsId;

    @ApiModelProperty(value = "创建时间", position = 22)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 23)
    private Date updateTime;

    @ApiModelProperty(value = "中空关系 ID（关联关系表）", position = 24)
    private Long relationId;

}

