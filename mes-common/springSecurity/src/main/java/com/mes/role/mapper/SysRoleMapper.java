package com.mes.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.role.SysRole;
import com.mes.role.vo.SysRoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /*
     * 查询角色列表
     *
     * @param key 关键字
     * @return 角色列表
     */
    List<SysRoleVO> roleList(String key);
}
