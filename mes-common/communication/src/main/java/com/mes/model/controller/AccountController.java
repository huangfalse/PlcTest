package com.mes.model.controller;

import com.mes.model.service.AccountService;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 账户表 前端控制器
 * </p>
 *
 * @author wu
 * @since 2024-08-28
 */
@Api(tags = "账户")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @ApiOperation("httpAPi测试")
    @PostMapping("/testApi")
    @ResponseBody
    public Result testApi(@RequestBody Map<String, Object> arguments) {
        String[] str = {"123", "456"};
        return Result.build(200, "连接成功", str);
    }

}

