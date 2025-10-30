package com.mes.vertical.queue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 大理片笼队列表(VerticalSheetCageQueueInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:53:37
 */
@ApiModel(description = "大理片笼队列表(VerticalSheetCageQueueInfo)表实体类")
@Data
public class VerticalSheetCageQueueInfo {

    @ApiModelProperty(value = "主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程 ID", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id", position = 3)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "玻璃 ID", position = 4)
    private String glassId;

    @ApiModelProperty(value = "流程卡号", position = 5)
    private String flowCardId;

    @ApiModelProperty(value = "玻璃类型", position = 6)
    private Integer glassType;

    @ApiModelProperty(value = "流程卡落架编码", position = 7)
    private String flowCardSequence;

    @ApiModelProperty(value = "宽度（单位：mm）", position = 8)
    private Double width;

    @ApiModelProperty(value = "高度（单位：mm）", position = 9)
    private Double height;

    @ApiModelProperty(value = "厚度（单位：mm）", position = 10)
    private Double thickness;

    @ApiModelProperty(value = "膜系 ID", position = 11)
    private String filmsId;

    @ApiModelProperty(value = "栅格号", position = 12)
    private Integer slot;

    @ApiModelProperty(value = "目标位置", position = 13)
    private Integer targetSlot;

    @ApiModelProperty(value = "状态：0 创建 ,2 已创建出片任务", position = 14)
    private Integer state;

    @ApiModelProperty(value = "车次号", position = 16)
    private Integer trainNumber;

    @ApiModelProperty(value = "创建时间", position = 17)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 18)
    private Date updateTime;

}

