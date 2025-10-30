package com.mes.role.vo;

import com.mes.menu.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2024/4/26 13:57
 * @Description:
 */
@ApiModel(description = "角色信息实体类")
@Data
public class SysRoleVO implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID", position = 2)
    private Long id;

    @ApiModelProperty(value = "角色名称", position = 3)
    private String name;

    @ApiModelProperty(value = "角色权限字符串", position = 4)
    private String roleKey;

    @ApiModelProperty(value = "角色状态（0正常 1停用）", position = 5)
    private String state;

    @ApiModelProperty(value = "删除标志", position = 6)
    private Integer delFlag;

    @ApiModelProperty(value = "备注", position = 7)
    private String remark;

    @ApiModelProperty(value = "角色菜单信息", position = 8)
    private List<SysMenu> menuList;

}
