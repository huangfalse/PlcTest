package com.mes.role.controller;

import com.mes.base.request.BaseRequest;
import com.mes.result.Result;
import com.mes.role.service.SysRoleService;
import com.mes.role.vo.SysRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhoush
 * @since 2024-04-11
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("新增角色及角色下的权限信息")
    @PostMapping("/saveRole")
    public Result<SysRoleVO> saveRole(@Validated @RequestBody SysRoleVO sysRoleVO) {
        sysRoleService.saveRole(sysRoleVO);
        return Result.build(200, "新增成功", sysRoleVO);
    }

    @ApiOperation("编辑角色及角色下的权限信息")
    @PostMapping("/updateRole")
    public Result<SysRoleVO> updateRole(@Validated @RequestBody SysRoleVO sysRoleVO) {
        sysRoleService.updateRole(sysRoleVO);
        return Result.build(200, "新增成功", sysRoleVO);
    }

    @ApiOperation("查询角色及角色下的权限信息")
    @PostMapping("/queryRole")
    public Result<List<SysRoleVO>> queryRole(@Validated @RequestBody BaseRequest request) {
        return Result.success(sysRoleService.queryRole(request));
    }


    @ApiOperation("删除角色及角色下的权限信息")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteRole(@RequestBody List<Long> ids) {
        return Result.build(200, "删除成功", sysRoleService.deleteRole(ids));
    }

}