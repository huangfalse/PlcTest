package com.mes.userinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.menu.SysMenu;
import com.mes.userinfo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /*
     * 根据用户id查询菜单列表
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenu> menuList(@Param("userId") Long id, String key);
}
