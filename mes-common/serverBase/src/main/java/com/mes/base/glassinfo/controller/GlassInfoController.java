package com.mes.base.glassinfo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.glassinfo.GlassInfo;
import com.mes.glassinfo.request.NotReportRequest;
import com.mes.result.Result;
import com.mes.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : yzx
 * @Date: 2025/7/23 15:39
 * @Description:
 */
@Api(tags = "小片数据")
@RestController
@RequestMapping("glassInfo")
public class GlassInfoController {
    /**
     * 服务对象
     */
    @Resource
    private GlassInfoService glassInfoService;

    @ApiOperation("根据工程号查询出未 【报工/破损】的 小片数据 (分页)")
    @PostMapping("/queryNotReport")
    @ResponseBody
    public Result<Page<GlassInfo>> queryNotReport(@RequestBody NotReportRequest request) {
        List<GlassInfo> list = glassInfoService.queryNotReport(request);
        Page<GlassInfo> result = (Page<GlassInfo>) PageUtil.listToPage(list, request.getPageNo(), request.getPageSize());
        return Result.build(200, "查询成功", result);
    }
}

