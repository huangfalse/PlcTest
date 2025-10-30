package com.mes.edging.queue.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.edging.queue.EdgQueueInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/6/16 20:30
 * @Description:
 */
@ApiModel(description = "前端主界面所有数据")
@Data
public class EdgIndexMessageVO {

    @ApiModelProperty(value = "磨边队列任务")
    Page<EdgQueueInfo> queueTaskList;
}
