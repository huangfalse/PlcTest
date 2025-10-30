package com.mes.base.formula.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.base.formula.service.HollowFormulaDetailsService;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.hollow.formula.request.HollowGlassFormulaRequest;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中空配方表(HollowFormulaDetails)表控制层
 *
 * @author makejava
 * @since 2025-07-08 16:01:34
 */
@Api(tags = "中空配方")
@RestController
@RequestMapping("hollowFormulaDetails")
public class HollowFormulaDetailsController {
    /**
     * 服务对象
     */
    @Resource
    private HollowFormulaDetailsService hollowFormulaDetailsService;

    @ApiOperation("获取配方信息（分页）")
    @PostMapping("/pageFormulaDetails")
    public Result<Page<HollowFormulaDetails>> pageFormulaDetails(@RequestBody HollowGlassFormulaRequest hollowGlassFormulaRequest) {
        return Result.success(
                hollowFormulaDetailsService.pageFormulaDetails(
                        hollowGlassFormulaRequest.getPageNo(),
                        hollowGlassFormulaRequest.getPageSize(),
                        hollowGlassFormulaRequest.getFormulaName()
                )
        );
    }

    @ApiOperation("新增数据")
    @PostMapping("/saveFormulaDetails")
    public Result saveFormulaDetails(@RequestBody HollowFormulaDetails hollowFormulaDetails) {
        return Result.success(hollowFormulaDetailsService.save(hollowFormulaDetails));
    }

    @ApiOperation("修改数据")
    @PostMapping("updateFormulaDetails")
    public Result updateFormulaDetails(@RequestBody HollowFormulaDetails hollowFormulaDetails) {
        return Result.success(this.hollowFormulaDetailsService.updateById(hollowFormulaDetails));
    }

    @ApiOperation("删除数据")
    @PostMapping("deleteFormulaDetails")
    public Result deleteFormulaDetails(@RequestBody HollowGlassFormulaRequest hollowGlassFormulaRequest) {
        return Result.success(this.hollowFormulaDetailsService.removeByIds(hollowGlassFormulaRequest.getIdList()));
    }

}

