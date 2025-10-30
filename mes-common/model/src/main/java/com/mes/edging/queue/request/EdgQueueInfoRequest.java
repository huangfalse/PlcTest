package com.mes.edging.queue.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/17 15:17
 * @Description:
 */
@ApiModel(description = "磨边队列请求参数")
@Data
public class EdgQueueInfoRequest extends PageRequest implements Serializable {

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "膜系", position = 2)
    private String filmsId;

    @ApiModelProperty(value = "线路", position = 3)
    private List<Integer> lineList;

    @ApiModelProperty(value = "磨边状态 0未磨边 1磨边中 2已磨边 8破损", position = 4)
    private List<Integer> stateList;

    @ApiModelProperty(value = "玻璃id", position = 5)
    private String glassId;

    @ApiModelProperty(value = "开始时间", position = 6)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", position = 7)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
