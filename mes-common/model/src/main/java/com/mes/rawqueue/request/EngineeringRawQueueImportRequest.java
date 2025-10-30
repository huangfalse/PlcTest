package com.mes.rawqueue.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工程原片队列表(EngineeringRawQueue)表实体类
 *
 * @author makejava
 * @since 2025-07-09 14:18:12
 */
@ApiModel(description = "工程原片队列表(EngineeringRawQueue)表实体类")
@Data
public class EngineeringRawQueueImportRequest {

    @ApiModelProperty(value = "工程号", position = 2)
    private String engineeringId;

    @ApiModelProperty(value = "膜系id", position = 3)
    private String filmsId;

    @ApiModelProperty(value = "宽", position = 4)
    private Double rawGlassWidth;

    @ApiModelProperty(value = "高", position = 5)
    private Double rawGlassHeight;

    @ApiModelProperty(value = "厚度", position = 6)
    private Double rawGlassThickness;

    @ApiModelProperty(value = "原片切割版图片序", position = 7)
    private Integer rawSequence;

}

