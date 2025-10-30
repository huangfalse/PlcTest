package com.mes.userinfo.controller;

import com.mes.base.request.BaseRequest;
import com.mes.result.Result;
import com.mes.userinfo.entity.SysUser;
import com.mes.userinfo.entity.vo.SysUserVO;
import com.mes.userinfo.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhoush
 * @since 2024-04-11
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody SysUser user) {
        return Result.success(sysUserService.login(user));
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('xt:yh')")
    public Result<String> logout() {
        return Result.success(sysUserService.logout());
    }

    @ApiOperation("新增用户信息")
    @PostMapping("/saveUser")
    public Result<String> saveUser(@Validated @RequestBody SysUserVO sysUser) {
        return Result.success(sysUserService.saveUser(sysUser));
    }


    @ApiOperation("更新用户信息")
    @PostMapping("/updateUser")
    public Result<List<SysUserVO>> updateUser(@Validated @RequestBody SysUserVO sysUser) {
        sysUserService.updateUser(sysUser);
        BaseRequest request = new BaseRequest();
        return Result.success(sysUserService.listByUserName(request));
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result resetPassword(String userId) {
        return Result.success(sysUserService.resetPassword(Long.parseLong(userId)));
    }


    @ApiOperation("获取用户列表")
    @PostMapping("/listByUserName")
    public Result<List<SysUserVO>> listByUserName(@RequestBody BaseRequest request) {
        return Result.success(sysUserService.listByUserName(request));
    }

    @ApiOperation("删除用户信息")
    @PostMapping("/deleteUser")
    public Result<String> deleteUser(@RequestBody List<Long> ids) {
        return Result.success(sysUserService.deleteUser(ids));
    }

}

