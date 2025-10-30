package com.mes.glassinfo;

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

@ApiModel(description = "玻璃信息表实体类")
@Data
public class GlassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "玻璃信息表id", position = 2)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流程卡", position = 3)
    private String flowCardId;

    @ApiModelProperty(value = "订单号", position = 4)
    private String orderNumber;

    @ApiModelProperty(value = "宽", position = 5)
    private Double width;

    @ApiModelProperty(value = "高", position = 6)
    private Double height;

    @ApiModelProperty(value = "厚度", position = 7)
    private Double thickness;

    @ApiModelProperty(value = "膜系", position = 8)
    private String filmsId;

    @ApiModelProperty(value = "层数", position = 9)
    private Integer layer;

    @ApiModelProperty(value = "总层数", position = 10)
    private int totalLayer;

    @ApiModelProperty(value = "磨前宽", position = 11)
    private double edgWidth;

    @ApiModelProperty(value = "磨前高", position = 12)
    private double edgHeight;

    @ApiModelProperty(value = "是否配片", position = 13)
    private Integer isMultiple;

    @ApiModelProperty(value = "配片最大宽", position = 14)
    private double maxWidth;

    @ApiModelProperty(value = "配片最大高", position = 15)
    private double maxHeight;

    @ApiModelProperty(value = "钢化是否接受横放", position = 16)
    private Integer isHorizontal;

    @ApiModelProperty(value = "原片顺序", position = 17)
    private Integer rawSequence;

    @ApiModelProperty(value = "x坐标", position = 18)
    private Integer xAxis;

    @ApiModelProperty(value = "y坐标", position = 19)
    private Integer yAxis;

    @ApiModelProperty(value = "钢化版图id", position = 20)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图片序", position = 21)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "x坐标", position = 22)
    private Integer xCoordinate;

    @ApiModelProperty(value = "y坐标", position = 23)
    private Integer yCoordinate;

    @ApiModelProperty(value = "旋转角度（逆时针）", position = 24)
    private double angle;

    @ApiModelProperty(value = "工程号", position = 25)
    private String engineerId;

    @ApiModelProperty(value = "生产规则id", position = 26)
    private Integer ruleId;

    @ApiModelProperty(value = "玻璃ID", position = 27)
    private String glassId;

    @ApiModelProperty(value = "是否合并", position = 28)
    private Integer combine;

    @ApiModelProperty(value = "打标属性", position = 29)
    private String markIcon;

    @ApiModelProperty(value = "除膜方式", position = 30)
    private Integer filmRemove;

    @ApiModelProperty(value = "落架次序", position = 31)
    private String flowCardSequence;

    @ApiModelProperty(value = "工艺流程", position = 32)
    private String process;

    @ApiModelProperty(value = "原片旋转角度", position = 33)
    private Integer rawAngle;

    @ApiModelProperty(value = "中空合片方向（0：无 ,1：外,2：内）", position = 34)
    private Integer hollowCombineDirection;

    @ApiModelProperty(value = "订单序号", position = 35)
    private Integer productSortNumber;

    @ApiModelProperty(value = "流程卡内顺序（从大到小）", position = 36)
    private Integer shelfOrder;

}
