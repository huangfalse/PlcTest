package com.mes.glassinfo.request;

import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/8/12 15:17
 * @Description:
 */
@Data
public class EngineerUnreportedRequest extends PageRequest {

    @ApiModelProperty(value = "工程id")
    private String engineerId;
}

