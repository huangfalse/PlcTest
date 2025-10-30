package com.mes.glassinfo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 11:12
 * @Description:
 */

@ApiModel(description = "玻璃流程卡信息实体类")
@Data
public class GlassInfoFlowCardVO implements Serializable {


    @ApiModelProperty(value = "总面积", position = 1)
    private Double glassTotalArea;

    @ApiModelProperty(value = "流程卡", position = 2)
    private String productName;

    @ApiModelProperty(value = "流程卡", position = 3)
    private String flowCardId;

    @ApiModelProperty(value = "层数", position = 4)
    private Integer layer;

    @ApiModelProperty(value = "订单序号", position = 5)
    private Integer productSortNumber;

    @ApiModelProperty(value = "宽", position = 6)
    private Double width;

    @ApiModelProperty(value = "高", position = 7)
    private Double height;

    @ApiModelProperty(value = "玻璃数量", position = 8)
    private Integer glassCount;

    @ApiModelProperty(value = "工程名称", position = 9)
    private String engineerName;

    @ApiModelProperty(value = "总玻璃数", position = 10)
    private String glassTotal;

    @ApiModelProperty(value = "工程厚度", position = 11)
    private Double glassThickness;


}
