package com.mes.menu.controller;

import com.mes.base.request.BaseRequest;
import com.mes.menu.SysMenu;
import com.mes.menu.service.SysMenuService;
import com.mes.result.Result;
import com.mes.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author zhoush
 * @since 2024-04-11
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        BaseRequest request = new BaseRequest();
        return Result.success(sysMenuService.getMenuTree(request));

    }

    @ApiOperation("修改菜单信息")
    @PostMapping("/updateMenu")
    public Result<List<SysMenu>> updateMenu(@Validated @RequestBody SysMenu sysMenu) {
        sysMenuService.updateMenu(sysMenu);
        BaseRequest request = new BaseRequest();
        return Result.build(200, "修改成功", sysMenuService.getMenuTree(request));
    }

    @ApiOperation("获取用户有权限的所有菜单")
    @GetMapping("/getMenuTree")
    public Result<List<SysMenu>> getMenuTree(BaseRequest request) {
        return Result.success(sysMenuService.getMenuTree(request));
    }

    @ApiOperation("获取用户的权限")
    @GetMapping("/getAuthorityInfo")
    public Result<List<String>> getAuthorityInfo() {
        return Result.success(sysMenuService.getAuthorityInfo());
    }


    @ApiOperation("用户当前用户的菜单和权限信息")
    @GetMapping("/nav")
    public Result<Map<Object, Object>> nav() {
        return Result.success(sysMenuService.nav());
    }

    @ApiOperation("删除菜单")
    @PostMapping("/deleteMenu")
    public Result<List<SysMenu>> deleteMenu(Long menuId) {
        BaseRequest request = new BaseRequest();
        return Result.build(200, sysMenuService.deleteMenu(menuId), sysMenuService.getMenuTree(request));
    }

    @ApiOperation("批量删除菜单")
    @PostMapping("/batchDeleteMenu")
    public Result<String> batchDeleteMenu(@RequestBody List<Long> menuIds) {
        return Result.success(sysMenuService.batchDeleteMenu(menuIds));
    }

    @ApiOperation("获取当前时间")
    @PostMapping("/getNowDate")
    public Result getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Result.build(200, "查询成功", sdf.format(DateUtil.getEndDate()));
    }
}

