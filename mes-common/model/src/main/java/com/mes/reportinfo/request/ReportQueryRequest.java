package com.mes.reportinfo.request;

import com.mes.base.request.TimeRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author wu
 * @since 2024-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportQueryRequest extends TimeRequest {

    @ApiModelProperty(value = "状态,1：未报工 3：已报工 7：已现补 8：已同步ERP")
    private Integer state;

    @ApiModelProperty(value = "工序")
    private String workingProcedure;

    @ApiModelProperty(value = "玻璃id")
    private String glassId;

}
