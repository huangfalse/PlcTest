package com.mes.damagedetails;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author tao
 * @since 2025-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DamageDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "破损类型")
    private String breakageType;

    @ApiModelProperty(value = "破损原因")
    private String breakageReason;

    @ApiModelProperty(value = "责任工序")
    private String responsibleProcess;

    @ApiModelProperty(value = "责任人员")
    private String responsiblePersonnel;

    @ApiModelProperty(value = "责任班组")
    private String responsibleTeam;

    @ApiModelProperty(value = "责任设备")
    private String responsibleEquipment;


}
