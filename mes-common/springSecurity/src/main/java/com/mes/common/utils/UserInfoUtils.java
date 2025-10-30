package com.mes.common.utils;

import com.mes.userinfo.entity.SysUser;

/**
 * @Author : zhoush
 * @Date: 2024/4/25 15:41
 * @Description:
 */
public class UserInfoUtils {
    private static InheritableThreadLocal<SysUser> tokenPool = new InheritableThreadLocal<SysUser>();

    public static SysUser get() {
        return tokenPool.get();
    }

    public static void set(SysUser user) {
        tokenPool.set(user);
    }

    public static void remove() {
        if (get() != null) {
            tokenPool.remove();
        }
    }
}
