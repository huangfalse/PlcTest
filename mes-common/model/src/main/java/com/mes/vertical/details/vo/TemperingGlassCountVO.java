package com.mes.vertical.details.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/30 10:50
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class TemperingGlassCountVO {

    @ApiModelProperty(value = "工程号", position = 1)
    private String engineerId;

    @ApiModelProperty(value = "钢化版图id", position = 2)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "膜系", position = 3)
    private String filmsId;

    @ApiModelProperty(value = "厚度", position = 4)
    private Double thickness;

    @ApiModelProperty(value = "总片数", position = 5)
    private Integer totalCount;

    @ApiModelProperty(value = "实际片数", position = 6)
    private Integer realCount;

    @ApiModelProperty(value = "破损片数", position = 7)
    private Integer damageCount;

    @ApiModelProperty(value = "缺少片数", position = 8)
    private Integer lackCount;
}
