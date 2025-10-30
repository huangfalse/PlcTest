package com.mes.damage.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mes.damage.Damage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class DamageReportRequest {

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

    @ApiModelProperty(value = "破损信息集合")
    private List<Damage> damageList;

}
