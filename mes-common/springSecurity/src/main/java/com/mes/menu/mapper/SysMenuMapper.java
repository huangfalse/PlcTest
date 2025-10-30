package com.mes.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.menu.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /*
     * 根据用户id查询权限
     *
     * @param parseLong
     * @return 权限列表
     */
    List<String> selectPermsByUserId(long parseLong);
}
