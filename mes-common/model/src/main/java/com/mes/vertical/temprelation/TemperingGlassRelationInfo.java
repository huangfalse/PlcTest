package com.mes.vertical.temprelation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 理片笼钢化关系表(TemperingGlassRelationInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:54:45
 */
@ApiModel(description = "理片笼钢化关系表(TemperingGlassRelationInfo)表实体类")
@Data
public class TemperingGlassRelationInfo {

    @ApiModelProperty(value = "理片笼玻璃分组表主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程号 ID", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "玻璃ID", position = 2)
    private String glassId;

    @ApiModelProperty(value = "钢化版图 ID", position = 3)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图中图片序号", position = 4)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "流程卡号", position = 3)
    private String flowCardId;

    @ApiModelProperty(value = "流程卡内顺序（从大到小）顺序", position = 4)
    private Integer shelfOrder;

    @ApiModelProperty(value = "层", position = 5)
    private Integer layer;

    @ApiModelProperty(value = "玻璃宽度（单位：mm）", position = 6)
    private Double width;

    @ApiModelProperty(value = "玻璃高度（单位：mm）", position = 7)
    private Double height;

    @ApiModelProperty(value = "玻璃厚度（单位：mm）", position = 8)
    private Double thickness;

    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    @ApiModelProperty(value = "分组炉号 ID", position = 9)
    private Integer virtualTemperingLayoutId;

    @ApiModelProperty(value = "分组编号（组号）", position = 10)
    private Integer virtualSlot;

    @ApiModelProperty(value = "分组内排序编号（组序）", position = 11)
    private Integer slotSequence;
    @ApiModelProperty(value = "状态", position = 12)
    private Integer state;

    @ApiModelProperty(value = "创建时间", position = 13)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 14)
    private Date updateTime;

}

