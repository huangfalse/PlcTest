package com.mes.edging.queue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 磨边队列(EdgQueueInfo)表实体类
 *
 * @author makejava
 * @since 2025-07-08 11:01:50
 */
@ApiModel(description = "磨边队列(EdgQueueInfo)表实体类")
@Data
public class EdgQueueInfo {

    @ApiModelProperty("磨边对列表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("玻璃ID")
    private String glassId;

    @ApiModelProperty("玻璃宽度（单位：毫米 * 10）")
    private Double width;

    @ApiModelProperty("玻璃高度（单位：毫米 * 10）")
    private Double height;

    @ApiModelProperty("玻璃厚度（单位：毫米 * 10）")
    private Double thickness;

    @ApiModelProperty("膜系编号")
    private String filmsId;

    @ApiModelProperty("设备ID")
    private Integer deviceId;

    @ApiModelProperty("线路编号")
    private Integer workLine;

    @ApiModelProperty("磨边状态（0=未磨边，1=磨边中，2=已磨边，8=破损）")
    private Integer state;

    @ApiModelProperty("玻璃key")
    private String glassIdKey;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}

