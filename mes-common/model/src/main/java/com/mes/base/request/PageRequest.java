package com.mes.base.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 16:12
 * @Description:
 */
@Data
public class PageRequest implements Serializable {

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
}
