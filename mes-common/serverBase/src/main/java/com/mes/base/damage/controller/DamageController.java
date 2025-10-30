package com.mes.base.damage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.base.damage.service.DamageService;
import com.mes.damage.Damage;
import com.mes.damage.dto.DamageDTO;
import com.mes.damage.request.DamageReportRequest;
import com.mes.damage.request.DamageRequest;
import com.mes.reportinfo.request.ReportQueryRequest;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author : zhoush
 * @Date: 2025/6/2 17:53
 * @Description:
 */
@Api(tags = "报工模块")
@RestController
@Slf4j
@RequestMapping("/damage")
public class DamageController {

    @Resource
    private DamageService damageService;

    @ApiOperation("添加破损数据")
    @PostMapping("/saveDamage")
    @ResponseBody
    public Result saveDamage(@RequestBody DamageDTO damageDTO) {
        damageService.saveDamage(damageDTO);
        return Result.success("success");
    }

    @ApiOperation("添加批量破损数据")
    @PostMapping("/saveBatchDamage")
    @ResponseBody
    public Result<Boolean> saveBatchDamage(@RequestBody DamageRequest request) {
        return Result.success(damageService.saveBatchDamage(request));
    }

    @ApiOperation("添加批量破损数据通过流程卡")
    @PostMapping("/saveBatchDamageByFlowCardId")
    @ResponseBody
    public Result<Boolean> saveBatchDamageByFlowCardId(@RequestBody DamageRequest request) {
        return Result.success(damageService.saveBatchDamageByFlowCardId(request));
    }

    @ApiOperation("查询破损数据")
    @PostMapping("/queryDamage")
    @ResponseBody
    public Result<Page<Damage>> queryDamage(@RequestBody ReportQueryRequest reportQueryRequest) {
        return Result.success(damageService.queryDamage(reportQueryRequest));
    }

    @ApiOperation("提交破损数据")
    @PostMapping("/submitRport")
    @ResponseBody
    public Result<Boolean> submitRport(@RequestBody DamageReportRequest damageReportRequest) {
        damageService.submitDamage(damageReportRequest);
        return Result.success(true);
    }

}
