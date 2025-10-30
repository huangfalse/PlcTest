package com.mes.load.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 上片任务表(LoadGlassHistoryTask)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:28:08
 */
@ApiModel(description = "上片任务表(LoadGlassHistoryTask)表实体类")
@Data
public class LoadGlassHistoryTask {

    @ApiModelProperty("历史任务id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("工程号")
    private String engineerId;

    @ApiModelProperty("工作线路/设备ID")
    private Integer workLine;

    @ApiModelProperty("原片宽")
    private Double rawGlassWidth;

    @ApiModelProperty("原片高")
    private Double rawGlassHeight;

    @ApiModelProperty("厚度")
    private Double rawGlassThickness;

    @ApiModelProperty("膜系")
    private String filmsId;

    @ApiModelProperty("上片总数量")
    private Integer totalCount;

    @ApiModelProperty("已完成数量")
    private Integer finishCount;

    @ApiModelProperty("破损数量")
    private Integer damageCount;

    @ApiModelProperty("任务状态 0默认空任务 1执行中 2结束任务")
    private Integer taskState;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;


}

