package com.mes.userinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.base.request.BaseRequest;
import com.mes.userinfo.entity.SysUser;
import com.mes.userinfo.entity.vo.SysUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    Map<String, String> login(SysUser user);

    /**
     * 退出登录
     *
     * @return
     */
    String logout();

    /**
     * 新增用户信息
     *
     * @param user
     * @return
     */
    String saveUser(SysUserVO user);

    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    SysUserVO updateUser(SysUserVO sysUser);

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    String resetPassword(Long userId);

    /**
     * 按名称获取用户信息
     *
     * @param userName
     * @return
     */
    SysUser queryByUserName(String userName);

    /**
     * 按关键字获取用户信息列表
     *
     * @param request
     * @return
     */
    List<SysUserVO> listByUserName(BaseRequest request);

    /**
     * 删除用户信息
     *
     * @param ids
     * @return
     */
    String deleteUser(List<Long> ids);

    /**
     * 获取用户角色权限信息
     *
     * @param userId
     * @return
     */
    List<String> getUserAuthorityInfo(Long userId);

    /**
     * 清空用户权限信息通过用户信息
     *
     * @param userName
     */
    void clearUserAuthorityInfo(String userName);

    /**
     * 清空用户权限信息通过角色id
     *
     * @param roleId
     */
    void clearUserAuthorityInfoByRoleId(Long roleId);

    /**
     * 清空用户权限信息通过菜单id
     *
     * @param menuId
     */
    void clearUserAuthorityInfoByMenuId(Long menuId);
}
