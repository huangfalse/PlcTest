package com.mes.horizontal.station;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 卧理理片笼表(HorizontalSheetCage)表实体类
 *
 * @author makejava
 * @since 2025-07-08 11:11:40
 */
@ApiModel(description = "卧理理片笼表(HorizontalSheetCage)表实体类")
@Data
public class HorizontalSheetCage {

    @ApiModelProperty("卧理理片笼表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("工序")
    private Integer processId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("栅格号")
    private Integer slot;

    @ApiModelProperty("启用状态")
    private Integer enableState;

    @ApiModelProperty("剩余宽度")
    private Double remainWidth;

    @ApiModelProperty("最大宽度")
    private Double maxWidth;


}

