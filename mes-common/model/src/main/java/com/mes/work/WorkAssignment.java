package com.mes.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 19:57
 * @Description:
 */
@ApiModel(description = "值班信息表(WorkAssignment)表实体类")
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "线路", position = 3)
    private Integer line;

    @ApiModelProperty(value = "工序", position = 4)
    private String workProcess;

    @ApiModelProperty(value = "班组名称", position = 5)
    private String teamsGroupsName;

    @ApiModelProperty(value = "设备名称", position = 6)
    private String deviceName;

}
