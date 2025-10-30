package com.mes.glassinfo.request;

import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author yzx
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotReportRequest extends PageRequest {

    @ApiModelProperty(value = "工程id")
    private String engineerId;

}
