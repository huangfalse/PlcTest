package com.mes.base.engineering.controller;

import com.mes.base.engineering.entity.request.EngineerImportRequest;
import com.mes.base.engineering.entity.request.EngineerRequest;
import com.mes.base.engineering.entity.vo.CutDrawingInfoVO;
import com.mes.base.engineering.service.EngineeringService;
import com.mes.base.rawqueue.service.EngineeringRawQueueService;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : zt
 * @Date: 2025/7/9 15:04
 * @Description:
 */
@Api(tags = "工程模块")
@RestController
@RequestMapping("engineering")
public class EngineeringController {
    /**
     * 服务对象
     */
    @Resource
    private EngineeringService engineeringService;
    @Resource
    private EngineeringRawQueueService engineeringRawQueueService;

    /**
     * 导入工程接口
     *
     * @param request 传入工程信息
     * @return 返回结果
     */
    @ApiOperation("导入工程列表")
    @PostMapping("/importEngineer")
    @ResponseBody
    public Result<Boolean> engineerImportList(@RequestBody EngineerImportRequest request) {
        //boolean success = true;
        boolean success = engineeringService.importEngineerList(request);
        if (success) {
            return Result.build(200, "任务导入成功", success);
        } else {
            return Result.build(200, "任务导入失败", success);
        }
    }

    /**
     * mes主动调用导入工程
     */
    @ApiOperation("mes主动调用导入工程")
    @PostMapping("/mesImportEngineer")
    @ResponseBody
    public Result<EngineerImportRequest> mesImportEngineer(@RequestBody EngineerRequest request) {
        return Result.build(200, "获取erp信息成功", engineeringService.mesImportEngineer(request));
    }

    /**
     * 查询工程号
     *
     * @return 未完成的工程号列表
     */
    @ApiOperation("查询未完成的工程列表-返回工程号")
    @PostMapping("/queryNotFinishEngineerIdList")
    @ResponseBody
    public Result<List<String>> queryNotFinishEngineerIdList() {
        List<String> success = engineeringService.queryNotFinishEngineerIdList();
        return Result.build(200, "查询成功", success);
    }

    /**
     * 查询工程任务并返回原片队列
     *
     * @param request
     * @return
     */
    @ApiOperation("查询工程任务并返回原片队列")
    @PostMapping("/queryEngineeringRawQueueList")
    @ResponseBody
    public Result<List<EngineeringRawQueue>> queryEngineeringRawQueueList(@RequestBody EngineerRequest request) {
        return Result.build(200, "查询成功", engineeringService.queryEngineeringRawQueueList(request));
    }

    /**
     * 开始任务接口
     *
     * @param request
     * @return
     */
    @ApiOperation("开始任务")
    @PostMapping("/startTask")
    @ResponseBody
    public Result<Boolean> startTask(@RequestBody EngineerRequest request) {
        Boolean isExe = engineeringService.checkEngineering(request);
        if (isExe && engineeringService.startTask(request)) {
            return Result.build(200, "开始任务成功", true);
        }
        return Result.build(200, "开始任务失败", false);
    }

    /**
     * 暂停任务接口
     *
     * @param request
     * @return
     */
    @ApiOperation("暂停任务")
    @PostMapping("/pauseTask")
    @ResponseBody
    public Result<Boolean> pauseTask(@RequestBody EngineerRequest request) {
        return Result.build(200, "暂停任务成功", engineeringService.pauseTask(request));
    }

    /**
     * 完成任务接口
     *
     * @param request
     * @return
     */
    @ApiOperation("完成任务")
    @PostMapping("/finishTask")
    @ResponseBody
    public Result<Boolean> finishTask(@RequestBody EngineerRequest request) {
        return Result.build(200, "结束任务成功", engineeringService.finishTask(request));
    }

    /**
     * 工程切割版图信息接口
     *
     * @param request
     * @return
     */
    @ApiOperation("工程切割版图信息")
    @PostMapping("/findCutPatten")
    @ResponseBody
    public Result<List<CutDrawingInfoVO>> findCutPatten(@RequestBody EngineerRequest request) {
        return Result.build(200, "查询工程切割版图信息成功", engineeringService.findCutPatten(request));
    }
}

