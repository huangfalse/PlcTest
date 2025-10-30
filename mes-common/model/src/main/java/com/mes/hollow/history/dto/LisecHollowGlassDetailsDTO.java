package com.mes.hollow.history.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/30 17:40
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class LisecHollowGlassDetailsDTO {

    @ApiModelProperty(hidden = true)
    private String recType;

    @ApiModelProperty(value = "项目数量指数（如果所有记录相同，则为0）	用0填充即可", position = 2)
    private String itemInx;

    @ApiModelProperty(value = "玻璃描述	任意字符填充", position = 3)
    private String descript;

    @ApiModelProperty(value = "玻璃表面类型（0浮动 1软涂层 2图案 3硬涂层）	与玻璃表面类型有关，填0-5之间的常数 用0填充即可", position = 4)
    private String surface;

    @ApiModelProperty(value = "10", position = 5)
    private String thickness;

    @ApiModelProperty(value = "涂层的侧面/图案化DGU(0 无面侧 1正面侧 2背面侧)	用0填充即可", position = 6)
    private String faceSide;

    @ApiModelProperty(value = "身份标识	用1填充即可", position = 7)
    private String ident;

    @ApiModelProperty(value = "玻璃板X的图案方向（0无图案 1水平图案 2垂直图案）	用0填充即可", position = 8)
    private String pattDir;

    @ApiModelProperty(value = "窗格标识（条形码）	自然数 条形码", position = 9)
    private String paneBcd;

    @ApiModelProperty(value = "窗格编号	用0填充即可", position = 10)
    private String prodPane;

    @ApiModelProperty(value = "组件编号	用0填充即可", position = 11)
    private String prodComp;

    @ApiModelProperty(value = "类别编号 材料类型（0未定义 1浮子 2层压 3防弹 4增韧 5浇祝树脂/防火装置）", position = 12)
    private String gategory;

}
