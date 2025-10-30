package com.mes.tempering.queue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 钢化队列表(TemperingGlassQueueInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:58:49
 */
@ApiModel(description = "钢化队列表(TemperingGlassQueueInfo)表实体类")
@Data
public class TemperingGlassQueueInfo {

    @ApiModelProperty(value = "钢化小片表主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备id", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "工程 ID", position = 3)
    private String engineerId;

    @ApiModelProperty(value = "玻璃 ID", position = 4)
    private String glassId;

    @ApiModelProperty(value = "流程卡号", position = 5)
    private String flowCardId;

    @ApiModelProperty(value = "玻璃类型", position = 6)
    private Integer glassType;

    @ApiModelProperty(value = "流程卡落架编码", position = 7)
    private String flowCardSequence;

    @ApiModelProperty(value = "玻璃宽度（单位：mm）", position = 8)
    private Double width;

    @ApiModelProperty(value = "玻璃高度（单位：mm）", position = 9)
    private Double height;

    @ApiModelProperty(value = "玻璃厚度（单位：mm）", position = 10)
    private Double thickness;

    @ApiModelProperty(value = "膜系 ID", position = 11)
    private String filmsId;

    @ApiModelProperty(value = "钢化版图 ID", position = 12)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图中图片序号", position = 13)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "X 坐标位置", position = 14)
    private Integer xCoordinate;

    @ApiModelProperty(value = "Y 坐标位置", position = 15)
    private Integer yCoordinate;

    @ApiModelProperty(value = "是否旋转（角度，单位：度）", position = 16)
    private Integer angle;

    @ApiModelProperty(value = "当前状态： -1：待出笼 0：出片完成 1：摆片完成 2：钢化完成 3：下片完成", position = 17)
    private Integer state;

    @ApiModelProperty(value = "栅格号", position = 18)
    private Integer slot;

    @ApiModelProperty(value = "创建时间", position = 19)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 20)
    private Date updateTime;

}

