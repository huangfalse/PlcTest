package com.mes.sysdict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wu
 * @since 2024-09-05
 */
@ApiModel(description = "<p> </p>")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    @ApiModelProperty(hidden = true)
    private Integer dictSort;

    @ApiModelProperty(hidden = true)
    private String dictLabel;

    @ApiModelProperty(hidden = true)
    private String dictValue;

    @ApiModelProperty(hidden = true)
    private String dictType;

    @ApiModelProperty(hidden = true)
    private String dictTypeValue;

    @ApiModelProperty(hidden = true)
    private String state;

    @ApiModelProperty(hidden = true)
    private String description;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @TableLogic
    private String deleted;

}
