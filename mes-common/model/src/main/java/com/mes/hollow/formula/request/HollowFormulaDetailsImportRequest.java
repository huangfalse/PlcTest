package com.mes.hollow.formula.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 中空配方表(HollowFormulaDetails)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:01:34
 */
@ApiModel(description = "中空配方表(HollowFormulaDetails)表实体类")
@Data
public class HollowFormulaDetailsImportRequest {

    @ApiModelProperty(value = "配方名称", position = 2)
    private String formulaName;

    @ApiModelProperty(value = "除膜方式（对应除膜工艺参数）", position = 3)
    private Integer filmRemove;

    @ApiModelProperty(value = "上侧除膜量（单位：mm）", position = 4)
    private Integer topRemove;

    @ApiModelProperty(value = "下侧除膜量（单位：mm）", position = 5)
    private Integer bottomRemove;

    @ApiModelProperty(value = "左侧除膜量（单位：mm）", position = 6)
    private Integer leftRemove;

    @ApiModelProperty(value = "右侧除膜量（单位：mm）", position = 7)
    private Integer rightRemove;

    @ApiModelProperty(value = "间隔板 1 编码", position = 8)
    private String frameOne;

    @ApiModelProperty(value = "间隔板 2 编码", position = 9)
    private String frameTwo;

    @ApiModelProperty(value = "间隔板 3 编码", position = 10)
    private String frameThree;

    @ApiModelProperty(value = "间隔板 4 编码", position = 11)
    private String frameFour;

    @ApiModelProperty(value = "密封胶嵌入深度（单位：mm）", position = 12)
    private String sealInsert;

    @ApiModelProperty(value = "气体注入 1（0：无，1：有）", position = 13)
    private String casOne;

    @ApiModelProperty(value = "气体注入 2（0：无，1：有）", position = 14)
    private String casTwo;

    @ApiModelProperty(value = "气体注入 3（0：无，1：有）", position = 15)
    private String casThree;

    @ApiModelProperty(value = "气体注入 4（0：无，1：有）", position = 16)
    private String casFour;

    @ApiModelProperty(value = "间隔框类型 1（例如：05 表示 TPS 胶，01 表示铝间隔条）", position = 17)
    private String intervalFrameTypeOne;

    @ApiModelProperty(value = "间隔框宽度 1（单位：mm）", position = 18)
    private String intervalFrameWidthOne;

    @ApiModelProperty(value = "间隔框高度 1（单位：mm）", position = 19)
    private String intervalFrameHeightOne;

    @ApiModelProperty(value = "间隔框类型 2", position = 20)
    private String intervalFrameTypeTwo;

    @ApiModelProperty(value = "间隔框宽度 2（单位：mm）", position = 21)
    private String intervalFrameWidthTwo;

    @ApiModelProperty(value = "间隔框高度 2（单位：mm）", position = 22)
    private String intervalFrameHeightTwo;

    @ApiModelProperty(value = "间隔框类型 3", position = 23)
    private String intervalFrameTypeThree;

    @ApiModelProperty(value = "间隔框宽度 3（单位：mm）", position = 24)
    private String intervalFrameWidthThree;

    @ApiModelProperty(value = "间隔框高度 3（单位：mm）", position = 25)
    private String intervalFrameHeightThree;

    @ApiModelProperty(value = "间隔框类型 4", position = 26)
    private String intervalFrameTypeFour;

    @ApiModelProperty(value = "间隔框宽度 4（单位：mm）", position = 27)
    private String intervalFrameWidthFour;

    @ApiModelProperty(value = "间隔框高度 4（单位：mm）", position = 28)
    private String intervalFrameHeightFour;

}

