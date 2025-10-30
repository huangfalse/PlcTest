package com.mes.userinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.NONE)
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;


}
