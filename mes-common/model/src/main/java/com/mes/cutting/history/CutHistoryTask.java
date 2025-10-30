package com.mes.cutting.history;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 切割历史任务(CutHistoryTask)表实体类
 *
 * @author makejava
 * @since 2025-07-09 08:40:21
 */
@ApiModel(description = "切割历史任务(CutHistoryTask)表实体类")
@Data
public class CutHistoryTask {

    @ApiModelProperty(value = "主键 ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工程号", position = 2)
    private String engineerId;

    @ApiModelProperty(value = "工作线路或设备 ID", position = 3)
    private Integer workLine;

    @ApiModelProperty(value = "膜系 ID（如 LOW-E、双银等）", position = 4)
    private String filmsId;

    @ApiModelProperty(value = "原片宽度（单位：mm）", position = 5)
    private Double rawGlassWidth;

    @ApiModelProperty(value = "原片高度（单位：mm）", position = 6)
    private Double rawGlassHeight;

    @ApiModelProperty(value = "原片厚度（单位：mm）", position = 7)
    private Double rawGlassThickness;

    @ApiModelProperty(value = "原片切割版图顺序（用于排序）", position = 8)
    private Integer rawSequence;

    @ApiModelProperty(value = "状态（可结合业务使用枚举描述具体含义）", position = 9)
    private Integer state;

    @ApiModelProperty(value = "原片排版文件路径（.opt 文件）", position = 10)
    private String optFileUrl;

    @ApiModelProperty(value = "创建时间", position = 11)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", position = 12)
    private Date updateTime;

}

