package com.mes.vertical.temperingtask;

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
@ApiModel(description = "钢化任务表(TemperingTask)表实体类")
@Data
public class TemperingTask {

    @ApiModelProperty(value = "主键 ID", position = 0)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父主键 ID：总记录", position = 1)
    private Long parentId;

    @ApiModelProperty(value = "设备号", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "工程 ID", position = 3)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id", position = 4)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "目标", position = 5)
    private Integer target;

    @ApiModelProperty(value = "是否强制出", position = 6)
    private Integer isForce;

    @ApiModelProperty(value = "状态", position = 7)
    private Integer state;
    @ApiModelProperty(value = "玻璃面积", position = 7)
    private Double area;
    @ApiModelProperty(value = "玻璃片数", position = 7)
    private Integer glassTotal;
    @ApiModelProperty(value = "装载率", position = 7)
    private Double ratio;
    @ApiModelProperty(value = "幕墙模式", position = 7)
    private Boolean curtainWall;

    @ApiModelProperty(value = "创建时间", position = 8)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 9)
    private Date updateTime;

}

