package com.mes.base.rawqueue.controller;



import com.mes.base.rawqueue.service.EngineeringRawQueueService;
import com.mes.rawglass.history.request.LoadGlassHistoryTaskUpdateRequest;
import com.mes.rawqueue.request.EngineeringRawQueueRequest;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
/**
 * @Author : yzx
 * @Date: 2025/7/23 15:10
 * @Description:
 */
@Api(tags = "原片版图信息")
@RestController
@RequestMapping("engineeringRawQueue")
public class EngineeringRawQueueController{
    /**
     * 服务对象
     */
    @Resource
    private EngineeringRawQueueService engineeringRawQueueService;

    @ApiOperation("修改状态,【0 待执行, 1,执行中,2执行完成")
    @PostMapping("/updateEngineeringRawQueue")
    @ResponseBody
    public Result<Boolean> updateEngineeringRawQueue(@RequestBody @Validated EngineeringRawQueueRequest request) {
        return Result.build(200, "查询成功", engineeringRawQueueService.updateEngineeringRawQueue(request));
    }

}

