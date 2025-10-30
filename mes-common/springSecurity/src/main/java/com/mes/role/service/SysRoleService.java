package com.mes.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.base.request.BaseRequest;
import com.mes.role.SysRole;
import com.mes.role.vo.SysRoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 创建角色信息
     *
     * @param sysRoleVO
     * @return
     */
    SysRole saveRole(SysRoleVO sysRoleVO);

    /**
     * 修改角色信息及权限
     *
     * @param sysRoleVO
     * @return
     */
    String updateRole(SysRoleVO sysRoleVO);

    /**
     * 按照角色名查询角色信息
     *
     * @param request
     * @return
     */
    List<SysRoleVO> queryRole(BaseRequest request);

    /**
     * 查询角色及角色下的权限信息
     *
     * @param ids
     * @return
     */
    String deleteRole(List<Long> ids);
}
