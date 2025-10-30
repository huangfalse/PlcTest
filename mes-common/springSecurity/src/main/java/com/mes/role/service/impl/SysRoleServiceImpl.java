package com.mes.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.request.BaseRequest;
import com.mes.menu.SysMenu;
import com.mes.role.SysRole;
import com.mes.role.SysRoleMenu;
import com.mes.role.mapper.SysRoleMapper;
import com.mes.role.service.SysRoleMenuService;
import com.mes.role.service.SysRoleService;
import com.mes.role.vo.SysRoleVO;
import com.mes.userinfo.entity.SysUserRole;
import com.mes.userinfo.service.SysUserRoleService;
import com.mes.userinfo.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole saveRole(SysRoleVO sysRoleVO) {
        log.info("保存角色信息，生成对应的角色id");
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleVO, sysRole);
        this.save(sysRole);
        //保存角色权限信息
        saveRoleMenu(sysRole.getId(), sysRoleVO.getMenuList());
        return sysRole;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRole(SysRoleVO sysRoleVO) {
        log.info("保存角色信息，生成对应的角色id");
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleVO, sysRole);
        this.updateById(sysRole);
        //保存角色权限信息
        return saveRoleMenu(sysRole.getId(), sysRoleVO.getMenuList());
    }

    @Override
    public List<SysRoleVO> queryRole(BaseRequest request) {
        return baseMapper.roleList(request.getKey());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRole(List<Long> ids) {
        log.info("删除角色信息");
        this.removeByIds(ids);

        log.info("删除中间表信息");
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getRoleId, ids));
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, ids));

        log.info("清空缓存中的权限信息");
        ids.stream().forEach(id -> sysUserService.clearUserAuthorityInfoByRoleId(id));
        return "success";
    }

    /**
     * 保存角色权限信息
     *
     * @param roleId
     * @param menuList
     * @return
     */
    private String saveRoleMenu(Long roleId, List<SysMenu> menuList) {
        log.info("配置角色菜单关系");
        if (CollectionUtils.isEmpty(menuList)) {
            // 先删除原来的记录，再保存新的
            sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
            // 删除缓存
            sysUserService.clearUserAuthorityInfoByRoleId(roleId);
            return "success";
        }
        List<SysRoleMenu> roleMenuList = menuList.stream().map(menu -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menu.getId());
            return roleMenu;
        }).collect(Collectors.toList());
        // 使用流处理菜单列表，提取 children 的 id，并设置到 SysRoleMenu 中
        List<SysRoleMenu> childrenList = menuList.stream()
                // 扁平化处理 children 列表
                .flatMap(menu -> menu.getChildren().stream())
                .map(child -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    // 设置 child 的 id 到 menuId
                    roleMenu.setMenuId(child.getId());
                    return roleMenu;
                })
                .collect(Collectors.toList());
        log.info("清空角色权限表中该角色信息");
        // 先删除原来的记录，再保存新的
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        sysRoleMenuService.saveBatch(roleMenuList);
        sysRoleMenuService.saveBatch(childrenList);
        // 删除缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);
        return "success";
    }
}
