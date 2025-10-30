package com.mes.userinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.request.BaseRequest;
import com.mes.common.utils.JwtUtil;
import com.mes.common.utils.UserInfoUtils;
import com.mes.menu.mapper.SysMenuMapper;
import com.mes.role.SysRole;
import com.mes.role.service.SysRoleService;
import com.mes.sysconst.Const;
import com.mes.userinfo.entity.LoginUser;
import com.mes.userinfo.entity.SysUser;
import com.mes.userinfo.entity.SysUserRole;
import com.mes.userinfo.entity.vo.SysUserVO;
import com.mes.userinfo.mapper.SysUserMapper;
import com.mes.userinfo.mapper.SysUserRoleMapper;
import com.mes.userinfo.service.SysUserRoleService;
import com.mes.userinfo.service.SysUserService;
import com.mes.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Map<String, String> login(SysUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.generateToken(userId);
        redisUtil.setCacheObject("login:" + userId, loginUser);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>(16);
        map.put("token", jwt);
        return map;
    }

    @Override
    public String logout() {
        log.info("用户退出");
        SysUser user = UserInfoUtils.get();
        redisUtil.deleteObject("login:" + user.getId());
        return "注销成功";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveUser(SysUserVO user) {
        log.info("保存用户信息");
        // 默认密码
        String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
        user.setPassword(password);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        this.save(sysUser);
        saveUserRole(user.getRoleList(), sysUser.getId());
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUserVO updateUser(SysUserVO user) {
        log.info("更新用户信息");
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        this.updateById(sysUser);
        log.info("删除用户角色信息");
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId()));
        log.info("保存用户角色信息");
        saveUserRole(user.getRoleList(), sysUser.getId());
        return user;
    }

    @Override
    public String resetPassword(Long userId) {
        log.info("重置密码为{}", Const.DEFULT_PASSWORD);
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
        sysUser.setPassword(password);
        this.updateById(sysUser);
        return "success";
    }

    @Override
    public SysUser queryByUserName(String userName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
    }

    @Override
    public List<SysUserVO> listByUserName(BaseRequest request) {
        return baseMapper.userList(request.getKey());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteUser(List<Long> ids) {
        this.removeByIds(ids);
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
        return "success";

    }

    @Override
    public List<String> getUserAuthorityInfo(Long userId) {
        SysUser sysUser = this.getById(userId);

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";
        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUserName())) {
            authority = redisUtil.getCacheObject("GrantedAuthority:" + sysUser.getUserName());

        } else {
            // 获取角色编码
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getRoleKey()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作编码
            List<String> perms = sysMenuMapper.selectPermsByUserId(userId);
            if (perms.size() > 0) {
                String menuPerms = String.join(",", perms);
                authority = authority.concat(menuPerms);
            }

            redisUtil.setCacheObject("GrantedAuthority:" + sysUser.getUserName(), authority, 60 * 60, TimeUnit.SECONDS);
        }
        return Arrays.stream(authority.split(",")).collect(Collectors.toList());
    }

    @Override
    public void clearUserAuthorityInfo(String userName) {
        redisUtil.deleteObject("GrantedAuthority:" + userName);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserName());
        });

    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = null;
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserName());
        });
    }

    /**
     * 实现UserDetailsService接口，从数据库内获取用户及权限信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getUserName, username);
        SysUser user = this.baseMapper.selectOne(lqw);
        //判断是否为空
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //查询权限信息
        List<String> perms = sysMenuMapper.selectPermsByUserId(user.getId());

        return new LoginUser(user, perms);
    }


    private void saveUserRole(List<SysRole> roles, Long userId) {
        log.info("保存用户角色信息");
        List<SysUserRole> userRoles = new ArrayList<>();
        if (CollectionUtils.isEmpty(roles)) {
            log.info("保存用户角色信息为空，给默认普通用户角色");
            userRoles.add(new SysUserRole(userId, Const.DEFULT_ROLE));
        } else {
            log.info("保存用户角色信息");
            userRoles = roles.stream().map(e -> new SysUserRole(userId, e.getId())).collect(Collectors.toList());
        }
        sysUserRoleService.saveBatch(userRoles);
    }

}
