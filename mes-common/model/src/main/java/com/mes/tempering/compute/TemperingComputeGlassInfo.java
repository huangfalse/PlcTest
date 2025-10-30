package com.mes.tempering.compute;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 钢化模拟计算小片详情表(TemperingComputeGlassInfo)表实体类
 *
 * @author makejava
 * @since 2025-09-08 17:36:35
 */
@ApiModel(description = "钢化模拟计算小片详情表(TemperingComputeGlassInfo)表实体类")
@Data
public class TemperingComputeGlassInfo implements Serializable {

    @ApiModelProperty(value = "钢化模拟计算小片详情表id", position = 0)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父主键 ID：总记录", position = 1)
    private Long parentId;

    @ApiModelProperty(value = "流程卡", position = 2)
    private String flowCardId;

    @ApiModelProperty(value = "总层数", position = 3)
    private Integer totalLayer;

    @ApiModelProperty(value = "层数", position = 4)
    private Integer layer;

    @ApiModelProperty(value = "钢化版图id", position = 5)
    private Integer temperingLayoutId;

    @ApiModelProperty(value = "钢化版图片序", position = 6)
    private Integer temperingFeedSequence;

    @ApiModelProperty(value = "宽", position = 7)
    private Double width;

    @ApiModelProperty(value = "高", position = 8)
    private Double height;

    @ApiModelProperty(value = "钢化x坐标", position = 9)
    private Integer xCoordinate;

    @ApiModelProperty(value = "钢化y坐标", position = 10)
    private Integer yCoordinate;

    @ApiModelProperty(value = "钢化旋转角度（逆时针）", position = 11)
    private Integer angle;

    @ApiModelProperty(value = "订单号", position = 12)
    private Integer productSortNumber;

    @ApiModelProperty(value = "玻璃id", position = 13)
    private String glassId;

    @ApiModelProperty(value = "创建时间", position = 14)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 15)
    private Date updateTime;

    @ApiModelProperty(value = "钢化工程id", position = 16)
    private String temperingEngineerId;

    @ApiModelProperty(value = "车次信息", position = 17)
    private Integer trainNumber;

    @ApiModelProperty(value = "厚度")
    private Double thickness;

    @ApiModelProperty(value = "膜系")
    private String filmsId;

    public String getKey() {
        return flowCardId + "," + layer + "," + productSortNumber;
    }

}

