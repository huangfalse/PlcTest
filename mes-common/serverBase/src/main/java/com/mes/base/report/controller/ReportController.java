package com.mes.base.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.base.report.service.ReportInfoService;
import com.mes.reportinfo.ReportInfo;
import com.mes.reportinfo.dto.ReportInfoDTO;
import com.mes.reportinfo.request.ReportQueryRequest;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/2 17:53
 * @Description:
 */
@Api(tags = "报工模块")
@RestController
@Slf4j
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportInfoService reportInfoService;

    @ApiOperation("添加完工数据")
    @PostMapping("/saveReport")
    @ResponseBody
    public Result saveReport(@RequestBody ReportInfoDTO reportInfoDTO) {
        reportInfoService.saveReport(reportInfoDTO);
        return Result.success("success");
    }

    @ApiOperation("查询完工数据")
    @PostMapping("/queryReport")
    @ResponseBody
    public Result<Page<ReportInfo>> queryReport(@RequestBody ReportQueryRequest reportQueryRequest) {
        return Result.success(reportInfoService.queryReport(reportQueryRequest));
    }

    @ApiOperation("提交完工数据")
    @PostMapping("/submitRport")
    @ResponseBody
    public Result<Boolean> submitRport(@RequestBody List<ReportInfo> reportInfos) {
        return Result.success(reportInfoService.submitReport(reportInfos));
    }

}
