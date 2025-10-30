package com.mes.vertical.details;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 立式理片笼详情表(VerticalSheetCageDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:52:24
 */
@ApiModel(description = "立式理片笼详情表(VerticalSheetCageDetails)表实体类")
@Data
public class VerticalSheetCageDetails {

    @ApiModelProperty(value = "大理片笼详情表主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备 ID", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "栅格号", position = 3)
    private Integer slot;

    @ApiModelProperty(value = "玻璃 ID", position = 4)
    private String glassId;

    @ApiModelProperty(value = "小片在格子内的顺序", position = 5)
    private Integer sequence;

    @ApiModelProperty(value = "流程卡号", position = 6)
    private String flowCardId;

    @ApiModelProperty(value = "流程卡落架编码", position = 7)
    private String flowCardSequence;

    @ApiModelProperty(value = "玻璃类型", position = 8)
    private Integer glassType;

    @ApiModelProperty(value = "玻璃宽度（单位：mm）", position = 9)
    private Double width;

    @ApiModelProperty(value = "玻璃高度（单位：mm）", position = 10)
    private Double height;

    @ApiModelProperty(value = "玻璃厚度（单位：mm）", position = 11)
    private Double thickness;

    @ApiModelProperty(value = "钢化版图 ID", position = 12)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图片序号", position = 13)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "X 坐标", position = 14)
    private Integer xCoordinate;

    @ApiModelProperty(value = "Y 坐标", position = 15)
    private Integer yCoordinate;

    @ApiModelProperty(value = "玻璃状态", position = 16)
    private Integer state;

    @ApiModelProperty(value = "玻璃间隙", position = 17)
    private Integer gap;

    @ApiModelProperty(value = "工程号 ID", position = 18)
    private String engineerId;

    @ApiModelProperty(value = "层号", position = 19)
    private Integer totalLayer;

    @ApiModelProperty(value = "层号", position = 20)
    private Integer layer;

    @ApiModelProperty(value = "创建时间", position = 21)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 22)
    private Date updateTime;

    @ApiModelProperty(value = "膜系 ID", position = 23)
    private String filmsId;

    @ApiModelProperty(value = "是否旋转（角度，单位：度）", position = 24)
    private Integer angle;

    @ApiModelProperty(value = "是否已钢化", position = 25)
    private Integer isTemp;

    @ApiModelProperty(value = "中空顺序", position = 26)
    private Integer hollowSequence;

    @ApiModelProperty(value = "是否配对", position = 27)
    private Integer isPair;

    @ApiModelProperty(value = "组号", position = 28)
    private Integer virtualSlot;

    @ApiModelProperty(value = "组序", position = 29)
    private Integer slotSequence;

    @ApiModelProperty(value = "落架顺序", position = 30)
    private Integer shelfOrder;
    @ApiModelProperty(value = "订单序号")
    private Integer productSortNumber;

    public String getKey() {
        return flowCardId + "," + layer + "," + productSortNumber;
    }

}

