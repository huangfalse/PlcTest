package com.mes.base.engineering.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.base.engineering.entity.request.EngineerImportRequest;
import com.mes.base.engineering.entity.request.EngineerRequest;
import com.mes.base.engineering.entity.vo.CutDrawingInfoVO;
import com.mes.engineering.Engineering;
import com.mes.rawqueue.EngineeringRawQueue;

import java.util.List;

/**
 * 工程表服务接口
 *
 * @author makejava
 * @since 2025-06-02 17:48:38
 */
public interface EngineeringService extends IService<Engineering> {

    /**
     * 导入工程信息
     *
     * @param request
     * @return
     */
    Boolean importEngineerList(EngineerImportRequest request);


    /**
     * 获取工程列表  未领取的任务
     *
     * @param request
     * @return
     */
    List<String> queryEngineerIdList(EngineerRequest request);

    /**
     * 获取未完工成 工程号 列表
     *
     * @return 工程号
     */
    List<String> queryNotFinishEngineerIdList();


    /**
     * 领取的工程任务并返回原片队列
     *
     * @param request
     * @return
     */
    List<EngineeringRawQueue> queryEngineeringRawQueueList(EngineerRequest request);

    /**
     * 领取的工程任务 按线路 返回 已领取的原片队列
     *
     * @param workLine
     * @return
     */
    List<EngineeringRawQueue> queryNotFinishEngineeringRawQueueList(Integer workLine);

    /**
     * 开始任务
     *
     * @param request
     * @return
     */
    Boolean startTask(EngineerRequest request);

    /**
     * 暂停任务
     *
     * @param request
     * @return
     */
    Boolean pauseTask(EngineerRequest request);

    /**
     * 完成任务
     *
     * @param request
     * @return
     */
    Boolean finishTask(EngineerRequest request);

    /**
     * 查询工程 切割版图信息
     *
     * @param request
     * @return
     */
    List<CutDrawingInfoVO> findCutPatten(EngineerRequest request);

    /**
     * 校验对应线路工程是否符合领取条件
     *
     * @param request
     * @return
     */
    boolean checkEngineering(EngineerRequest request);

    /**
     * mes主动调用导入工程
     *
     * @param request
     * @return
     */
    EngineerImportRequest mesImportEngineer(EngineerRequest request);
}

