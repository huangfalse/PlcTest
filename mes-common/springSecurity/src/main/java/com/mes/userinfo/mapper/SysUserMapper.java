package com.mes.userinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.userinfo.entity.SysUser;
import com.mes.userinfo.entity.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /*
     * @Description: 查询用户列表
     *
     * @param key 查询条件
     * @return 用户列表
     */
    List<SysUserVO> userList(@Param("key") String key);
}
