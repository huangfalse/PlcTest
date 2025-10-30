package com.mes.hollow.history.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/12/30 17:40
 * @Description:
 */
@ApiModel(description = "中空铝框信息")
@Data
public class LisecHollowFrameDetailsDTO {

    public LisecHollowFrameDetailsDTO(String recType, String type, String width, String height) {
        this.recType = recType;
        this.type = type;
        this.width = width;
        this.height = height;
        this.itemInx = "0";
        this.descript = "1";
        this.color = "0";
        this.ident = "";
        this.frameBcd = "";
    }

    @ApiModelProperty(hidden = true)
    private String recType;

    @ApiModelProperty(value = "项目数量指数（如果所有记录相同，则为0）	用0填充即可", position = 2)
    private String itemInx;

    @ApiModelProperty(value = "描述	任意字符填充", position = 3)
    private String descript;

    @ApiModelProperty(value = "框架类型（0铝 1钢 2不锈钢 3塑料 4超间隔器 5tps 6t型垫片 7duralite 8duraseal 9瑞士菜 10iet 11gss 12triseal 13舒科 14chromatech  ）	间隔框类型，TPS胶为05，铝间隔条为01", position = 4)
    private String type;

    @ApiModelProperty(value = "颜色	用0填充即可", position = 5)
    private String color;

    @ApiModelProperty(value = "10	间隔框宽度", position = 6)
    private String width;

    @ApiModelProperty(value = "10	间隔框高度", position = 7)
    private String height;

    @ApiModelProperty(value = "身份标识", position = 8)
    private String ident;

    @ApiModelProperty(value = "条形码", position = 9)
    private String frameBcd;

}
