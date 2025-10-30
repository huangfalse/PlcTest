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
public class HorizontalDetailsRequest {

    @ApiModelProperty("工程id")
    private String engineerId;
    @ApiModelProperty("玻璃id")
    private String glassId;
    @ApiModelProperty("流程卡号")
    private String flowCardId;
    @ApiModelProperty("状态")
    private Integer state;
    @ApiModelProperty("格子")
    private Integer slot;
    @ApiModelProperty("工序")
    private String workingProcedure;
    @ApiModelProperty("工序")
    private Integer workLine;

}


