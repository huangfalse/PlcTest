package com.mes.engineering;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工程表(Engineering)表实体类
 *
 * @author makejava
 * @since 2025-07-09 14:21:56
 */
@ApiModel(description = "工程表(Engineering)表实体类")
@Data
public class Engineering {

    @ApiModelProperty(value = "工程表id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程号", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "工程名称", position = 3)
    private String engineerName;

    @ApiModelProperty(value = "平均利用率", position = 4)
    private String avgAvailability;

    @ApiModelProperty(value = "有效利用率", position = 5)
    private String validAvailability;

    @ApiModelProperty(value = "尾片利用率", position = 6)
    private String lastAvailability;

    @ApiModelProperty(value = "状态", position = 7)
    private Integer state;

    @ApiModelProperty(value = "小片总数", position = 8)
    private Integer glassTotal;

    @ApiModelProperty(value = "小片总面积", position = 9)
    private Double glassTotalArea;

    @ApiModelProperty(value = "计划原片总数", position = 10)
    private Integer planPatternTotal;

    @ApiModelProperty(value = "计划原片总面积", position = 11)
    private Double planPatternTotalArea;

    @ApiModelProperty(value = "实际原片总数", position = 12)
    private Integer realityPatternTotal;

    @ApiModelProperty(value = "实际原片总面积", position = 13)
    private Double realityPatternTotalArea;

    @ApiModelProperty(value = "膜系id", position = 14)
    private String filmsId;

    @ApiModelProperty(value = "备注", position = 15)
    private String notes;

    @ApiModelProperty(value = "工作线路", position = 16)
    private Integer workLine;

    @ApiModelProperty(value = "创建时间", position = 17)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 18)
    private Date updateTime;

}

