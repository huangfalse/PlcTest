package com.mes.userinfo.entity.vo;

import com.mes.role.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@ApiModel(description = "用户信息实体类")
@Data
public class SysUserVO implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", position = 2)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 3)
    private String userName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", position = 4)
    private String nickName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", position = 5)
    private String password;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", position = 6)
    private String avatar;

    /**
     * 用户的角色信息
     */
    @ApiModelProperty(value = "用户的角色信息", position = 7)
    private List<SysRole> roleList;


}
