package com.mes.rawglass.history.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上片工位表(LoadStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:32
 */
@ApiModel(description = "上片任务类型请求")
@Data
public class LoadGlassHistoryTaskUpdateRequest {

    @ApiModelProperty("任务id")
    private Long id;

    @ApiModelProperty("任务状态 0默认空任务 1执行中 2结束任务")
    private Integer taskState;

}

