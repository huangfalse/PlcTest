package com.mes.damage.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wu
 * @since 2024-06-25
 */
@Data
public class DamageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 流程卡报破损
     */
    @ApiModelProperty("流程卡号")
    private String flowCardId;

    @ApiModelProperty("订单序号")
    private Integer glassType;

    @ApiModelProperty("层号")
    private Integer layer;


    /**
     * 玻璃小片报破损
     */
    @ApiModelProperty(value = "玻璃id")
    private List<String> glassIdList;

    /**
     * 破损基础信息
     */
    @ApiModelProperty(value = "线路")
    private Integer line;

    @ApiModelProperty(value = "工序")
    private String workingProcedure;

    @ApiModelProperty(value = "备注")
    private String remark;

}
