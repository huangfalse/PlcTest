package com.mes.hollow.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 中空历史任务表(HollowGlassTaskHistory)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:10
 */
@ApiModel(description = "中空历史任务表(HollowGlassTaskHistory)表实体类")
@Data
public class HollowGlassTaskHistory {

    @ApiModelProperty(value = "中空玻璃关系表主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程卡号", position = 2)
    private String flowCardId;

    @ApiModelProperty(value = "总层数", position = 3)
    private Integer totalLayer;

    @ApiModelProperty(value = "路线（表示所属生产线或工艺路径）", position = 4)
    private Integer cell;

    @ApiModelProperty(value = "状态： 0 - 新增 1 - 执行中 2 - 暂停 3 - 结束", position = 5)
    private Integer state;

    @ApiModelProperty(value = "总配对数量", position = 6)
    private Integer totalPairQuantity;

    @ApiModelProperty(value = "已完成配对的数量", position = 7)
    private Integer pairQuantity;

    @ApiModelProperty(value = "是否强制执行（0 - 否，1 - 是）", position = 8)
    private Integer isForce;

    @ApiModelProperty(value = "配方 ID（关联配方表）", position = 9)
    private Integer formulaId;

    @ApiModelProperty(value = "创建时间（任务开始时记录）", position = 10)
    private Date createTime;

    @ApiModelProperty(value = "更新时间（每次状态变更时更新）", position = 11)
    private Date updateTime;

}

