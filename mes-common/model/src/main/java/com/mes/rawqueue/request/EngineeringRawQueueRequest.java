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
public class EngineeringRawQueueRequest {

    @ApiModelProperty(value = "工程原片队列表id", position = 1)
    private Long id;

    @ApiModelProperty(value = "状态", position = 8)
    private Integer state;
}

