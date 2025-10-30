package com.mes.vertical.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 立式理片笼历史任务表(VerticalSheetCageHistoryTask)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:53:15
 */
@ApiModel(description = "立式理片笼历史任务表(VerticalSheetCageHistoryTask)表实体类")
@Data
public class VerticalSheetCageHistoryTask {

    @ApiModelProperty(value = "主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "玻璃 ID", position = 2)
    private String glassId;

    @ApiModelProperty(value = "起始格子编号", position = 3)
    private Integer startSlot;

    @ApiModelProperty(value = "目标格子编号", position = 4)
    private Integer targetSlot;

    @ApiModelProperty(value = "长边", position = 5)
    private Double width;

    @ApiModelProperty(value = "短边", position = 6)
    private Double height;

    @ApiModelProperty(value = "厚度", position = 7)
    private Double thickness;

    @ApiModelProperty(value = "靠边距", position = 8)
    private Integer edgeDistance;

    @ApiModelProperty(value = "目标靠边距", position = 9)
    private Integer targetEdgeDistance;

    @ApiModelProperty(value = "任务状态", position = 10)
    private Integer taskState;

    @ApiModelProperty(value = "玻璃数量", position = 11)
    private Integer glassCount;

    @ApiModelProperty(value = "任务类型", position = 12)
    private Integer taskType;

    @ApiModelProperty(value = "货车数量", position = 13)
    private Integer wagonNumber;

    @ApiModelProperty(value = "车次信息", position = 14)
    private Integer trainNumber;

    @ApiModelProperty(value = "创建时间", position = 15)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 16)
    private Date updateTime;

}

