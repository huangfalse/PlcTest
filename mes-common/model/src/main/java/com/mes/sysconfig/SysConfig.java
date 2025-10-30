package com.mes.sysconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * (SysConfig)表实体类
 *
 * @author makejava
 * @since 2025-04-01 15:01:22
 */
@ApiModel(description = "(SysConfig)表实体类")
@Data
public class SysConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父配置id(弃用)", position = 2)
    private Long parentId;

    @ApiModelProperty(value = "配置名", position = 3)
    private String configName;

    @ApiModelProperty(value = "配置代码", position = 4)
    private String configCode;

    @ApiModelProperty(value = "配置值", position = 5)
    private String configValue;

    @ApiModelProperty(value = "是否启用", position = 6)
    private Integer isEnable;

    @ApiModelProperty(value = "创建时间", position = 7)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 8)
    private Date updateTime;

}

