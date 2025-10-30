package com.mes.tempering.compute;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 钢化模拟计算记录表(TemperingComputeRecord)表实体类
 *
 * @author makejava
 * @since 2025-09-08 17:36:36
 */
@ApiModel(description = "钢化模拟计算记录表(TemperingComputeRecord)表实体类")
@Data
public class TemperingComputeRecord implements Serializable {

    @ApiModelProperty(value = "钢化模拟计算记录表id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "钢化工程id", position = 2)
    private String temperingEngineerId;

    @ApiModelProperty(value = "循环落架数", position = 3)
    private Integer guidance;

    @ApiModelProperty(value = "版图记录", position = 4)
    private String rackInfos;

    @ApiModelProperty(value = "总炉数", position = 5)
    private Integer totalTempering;

    @ApiModelProperty(value = "平均装载率", position = 6)
    private Double loadingRate;

    @ApiModelProperty(value = "模拟mes接口入参", position = 7)
    private String request;

    @ApiModelProperty(value = "钢化计算结果", position = 7)
    private String resultMessage;

    @ApiModelProperty(value = "钢化模拟计算接口入参", position = 7)
    private String tempRequest;

    @ApiModelProperty(value = "0计算完成  1开始钢化 2钢化完成 3作废", position = 8)
    private Integer state;

    @ApiModelProperty(value = "创建时间：开始钢化时更新", position = 9)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 10)
    private Date updateTime;

    @ApiModelProperty(value = "炉宽", position = 11)
    private Integer furnaceWidth;

    @ApiModelProperty(value = "炉高", position = 12)
    private Integer furnaceHeight;

}

