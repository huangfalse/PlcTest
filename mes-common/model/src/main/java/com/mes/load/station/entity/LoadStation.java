package com.mes.load.station.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 上片工位表(LoadStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:32
 */
@ApiModel(description = "上片工位表(LoadStation)表实体类")
@Data
public class LoadStation {

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("栅格号")
    private Integer slot;

    @ApiModelProperty("启用/禁用")
    private Integer enableState;


}

