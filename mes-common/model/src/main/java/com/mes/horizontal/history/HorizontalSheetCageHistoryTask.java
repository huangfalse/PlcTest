package com.mes.horizontal.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 卧理理片笼历史任务表(HorizontalSheetCageHistoryTask)表实体类
 *
 * @author makejava
 * @since 2025-07-08 11:13:25
 */
@ApiModel(description = "卧理理片笼历史任务表(HorizontalSheetCageHistoryTask)表实体类")
@Data
public class HorizontalSheetCageHistoryTask {

    @ApiModelProperty("卧理历史任务id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("玻璃id")
    private String glassId;

    @ApiModelProperty("宽*10")
    private Integer width;

    @ApiModelProperty("高*10")
    private Integer height;

    @ApiModelProperty("厚度*10")
    private Integer thickness;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("线路")
    private Integer workLine;

    @ApiModelProperty("任务状态 0开始任务 1结束任务")
    private Integer taskState;

    @ApiModelProperty("任务类型")
    private Integer taskType;

    @ApiModelProperty("当前格子")
    private Integer currentSlot;

    @ApiModelProperty("开始格子")
    private Integer startSlot;

    @ApiModelProperty("目标格子")
    private Integer targetSlot;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}

