package com.mes.sysdict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysDictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    @ApiModelProperty(hidden = true)
    private String dictName;

    @ApiModelProperty(hidden = true)
    private String dictType;

    @ApiModelProperty(hidden = true)
    private String state;

    @ApiModelProperty(hidden = true)
    private String description;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    @TableLogic
    private String deleted;


}
