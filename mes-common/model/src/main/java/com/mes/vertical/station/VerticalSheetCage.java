package com.mes.vertical.station;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 立式理片笼表(VerticalSheetCage)表实体类
 *
 * @author makejava
 * @since 2025-07-08 15:44:21
 */
@ApiModel(description = "立式理片笼表(VerticalSheetCage)表实体类")
@Data
public class VerticalSheetCage {

    @ApiModelProperty(value = "大理片笼主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工序 ID（如磨边、钢化等）", position = 2)
    private Integer processId;

    @ApiModelProperty(value = "设备 ID（关联具体设备）", position = 3)
    private Integer deviceId;

    @ApiModelProperty(value = "栅格号（物理位置编号）", position = 4)
    private Integer slot;

    @ApiModelProperty(value = "可存玻璃的最小厚度（单位：mm）", position = 5)
    private Integer minThickness;

    @ApiModelProperty(value = "可存玻璃的最大厚度（单位：mm）", position = 6)
    private Integer maxThickness;

    @ApiModelProperty(value = "可存玻璃的最大高度（单位：mm）", position = 7)
    private Integer maxHeight;

    @ApiModelProperty(value = "启用状态（0 表示禁用，1 表示启用）", position = 8)
    private Integer enableState;

    @ApiModelProperty(value = "当前剩余可用宽度（单位：mm）", position = 9)
    private Integer remainWidth;

    @ApiModelProperty(value = "左边距", position = 10)
    private Integer leftCoordinate;

    @ApiModelProperty(value = "右边距", position = 11)
    private Integer rightCoordinate;

}

