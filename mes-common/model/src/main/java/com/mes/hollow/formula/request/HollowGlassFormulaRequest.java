package com.mes.hollow.formula.request;

import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2024/12/30 15:34
 * @Description:
 */
@ApiModel(description = "中空配方信息及玻璃信息用于发送除膜数据")
@Data
public class HollowGlassFormulaRequest extends PageRequest {

    @ApiModelProperty(value = "配方名称", position = 1)
    private String formulaName;

    @ApiModelProperty(value = "配方id集合", position = 2)
    private List<Long> idList;

}

