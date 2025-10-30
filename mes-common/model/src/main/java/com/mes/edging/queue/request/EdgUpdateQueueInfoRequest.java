package com.mes.edging.queue.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/6/17 15:17
 * @Description:
 */
@ApiModel(description = "磨边队列请求参数")
@Data
public class EdgUpdateQueueInfoRequest implements Serializable {

    @ApiModelProperty("磨边对列表ID")
    private Long id;
    @ApiModelProperty("磨边状态（0=未磨边，1=磨边中，2=已磨边，8=破损）")
    private Integer state;

}
