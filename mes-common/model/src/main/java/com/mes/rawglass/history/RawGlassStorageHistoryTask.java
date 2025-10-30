package com.mes.rawglass.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * (RawGlassStorageHistoryTask)表实体类
 *
 * @author makejava
 * @since 2025-07-05 22:09:10
 */
@Data
public class RawGlassStorageHistoryTask {

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "起始格子", position = 2)
    private Integer startSlot;

    @ApiModelProperty(value = "目标格子", position = 3)
    private Integer endSlot;

    @ApiModelProperty(value = "第一原片边距", position = 4)
    private Integer rawMargin;

    @ApiModelProperty(value = "原片宽", position = 6)
    private Double rawGlassWidth;

    @ApiModelProperty(value = "原片高", position = 7)
    private Double rawGlassHeight;

    @ApiModelProperty(value = "厚度", position = 8)
    private Double rawGlassThickness;

    @ApiModelProperty(value = "膜系", position = 9)
    private String filmsId;

    @ApiModelProperty(value = "原片数量", position = 10)
    private Integer rawQuantity;

    @ApiModelProperty(value = "任务类型", position = 11)
    private Integer taskType;

    @ApiModelProperty(value = "任务状态", position = 12)
    private Integer taskState;

    @ApiModelProperty(value = "架子号", position = 13)
    private Integer shelf;

    @ApiModelProperty(value = "创建时间", position = 14)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 15)
    private Date updateTime;

}

