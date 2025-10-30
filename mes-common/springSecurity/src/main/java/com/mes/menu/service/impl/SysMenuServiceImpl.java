package com.mes.menu.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.request.BaseRequest;
import com.mes.common.utils.UserInfoUtils;
import com.mes.menu.SysMenu;
import com.mes.menu.mapper.SysMenuMapper;
import com.mes.menu.service.SysMenuService;
import com.mes.role.SysRoleMenu;
import com.mes.role.service.SysRoleMenuService;
import com.mes.userinfo.entity.SysUser;
import com.mes.userinfo.mapper.SysUserRoleMapper;
import com.mes.userinfo.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@Service
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserService sysUserService;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Override
    public SysMenu updateMenu(SysMenu menu) {
        baseMapper.updateById(menu);
        // 清除所有与该菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(menu.getId());
        return menu;
    }

    @Override
    public List<SysMenu> getMenuTree(BaseRequest request) {
        //需要先获取用户的角色，角色下的菜单权限，拿到菜单id获取所有菜单
        SysUser user = UserInfoUtils.get();
        //查询用户所拥有的所有权限信息
        List<SysMenu> menuList = sysUserRoleMapper.menuList(user.getId(), request.getKey());
        log.info("userinfos:{}", menuList);
        return create(menuList);
    }

    @Override
    public List<String> getAuthorityInfo() {
        SysUser user = UserInfoUtils.get();
        log.info("获取用户信息，用户名为{}", user);
        // 获取权限信息
        // ROLE_admin,ROLE_normal,sys:user:list,....
        return sysUserService.getUserAuthorityInfo(user.getId());
    }

    @Override
    public Map<Object, Object> nav() {
        List<SysMenu> menuTree = getMenuTree(new BaseRequest());
        List<String> authorityInfo = getAuthorityInfo();
        return MapUtil.builder().put("authoritys", authorityInfo)
                .put("tree", menuTree).map();
    }

    @Override
    public String deleteMenu(Long menuId) {
        int count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId));
        if (count > 0) {
            return "无法删除,请先删除子菜单";
        }

        // 清除所有与该菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(menuId);

        this.removeById(menuId);

        // 同步删除中间关联表
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));
        return "删除成功";
    }

    @Override
    public String batchDeleteMenu(List<Long> menuIds) {
        menuIds.stream().forEach(e -> deleteMenu(e));
        return "批量删除成功";
    }


    /**
     * 将数据库中查询出来的list集合传入此方法即可获得排成树形结构的list集合
     *
     * @param lists
     * @return
     */
    public List<SysMenu> create(List<SysMenu> lists) {
        List<SysMenu> deptTreeList = lists.stream()
                .filter(item -> item.getParentId() == 0)
                .map(item -> {
                    item.setChildren(getChildren(item, lists));
                    return item;
                }).collect(Collectors.toList());
        return deptTreeList;
    }

    /**
     * 此方法将被递归调用
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<SysMenu> getChildren(SysMenu menu, List<SysMenu> menus) {
        List<SysMenu> res = menus.stream()
                .filter(item -> item.getParentId().equals(menu.getId()))
                .collect(Collectors.toList());
        log.info("菜单树:{}", JSONUtil.toJsonStr(res));
        return res;
    }

}
