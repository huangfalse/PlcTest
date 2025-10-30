package com.mes.reportinfo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class ReportInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "玻璃id")
    private String glassId;

    @ApiModelProperty(value = "线路")
    private Integer line;

    @ApiModelProperty(value = "工序")
    private String workingProcedure;

    @ApiModelProperty(value = "备注")
    private String remark;

}
