package com.mes.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.base.request.BaseRequest;
import com.mes.menu.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 更新菜单信息
     *
     * @param menu
     * @return
     */
    SysMenu updateMenu(SysMenu menu);

    /**
     * 获取用户有权限的菜单树
     *
     * @param request
     * @return
     */
    List<SysMenu> getMenuTree(BaseRequest request);

    /**
     * 获取用户的权限
     *
     * @return
     */
    List<String> getAuthorityInfo();


    /**
     * 获取用户菜单及权限信息
     *
     * @return
     */
    Map<Object, Object> nav();

    /**
     * 删除菜单信息
     *
     * @param menuId
     * @return
     */
    String deleteMenu(Long menuId);

    /**
     * 批量删除菜单信息
     *
     * @param menuIds
     * @return
     */
    String batchDeleteMenu(List<Long> menuIds);

}
