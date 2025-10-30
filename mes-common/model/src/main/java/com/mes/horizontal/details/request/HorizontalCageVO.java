package com.mes.horizontal.details.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/26 20:40
 * @Description:
 */
@ApiModel(description = "卧理请求")
@Data
public class HorizontalCageVO {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("玻璃id")
    private Integer enableState;

}


