package com.mes.rawglass.station;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (RawGlassStorageStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:10:04
 */
@ApiModel(description = "(RawGlassStorageStation)表实体类")
@Data
public class RawGlassStorageStation {
    
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工位号", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "栅格号", position = 3)
    private Integer slot;

    @ApiModelProperty(value = "启用/禁用", position = 4)
    private Integer enableState;

    @ApiModelProperty(value = "架子号", position = 5)
    private Integer shelf;

}

