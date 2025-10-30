package com.mes.base.rawqueue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.rawqueue.request.EngineeringRawQueueRequest;

import java.util.List;

/**
 * 工程原片队列表(EngineeringRawQueue)表服务接口
 *
 * @author makejava
 * @since 2025-07-09 14:18:12
 */
public interface EngineeringRawQueueService extends IService<EngineeringRawQueue> {
    /**
     * 获取工程 工程列表数据的所有原片信息
     *
     * @param listEngineeringId
     * @return
     */
    List<EngineeringRawQueue> queryEngineeringRawQueueList(List<String> listEngineeringId);

    /**
     * 获取工程 工程列表数据的所有未完成的原片信息
     *
     * @param listEngineeringId
     * @return
     */
    List<EngineeringRawQueue> queryNotFinishEngineeringRawQueueList(List<String> listEngineeringId);

    /**
     * 修改原片版图状态
     *
     * @param request 原片版图ID
     * @return 修改结果
     */
    boolean updateEngineeringRawQueue(EngineeringRawQueueRequest request);

    /**
     * 获取当前工程需要调度的原片尺寸及数量（最多两片）
     *
     * @param engineerId
     * @return
     */
    List<EngineeringRawQueue> queryRawQueueList(String engineerId);

    /**
     * 按照工程及数量更新原片队列的状态
     *
     * @param engineerId
     * @param taskStateList
     * @param finishTaskState
     * @param count
     */
    void updateRawQueueByCount(String engineerId, List<Integer> taskStateList, Integer finishTaskState, int count);

    /**
     * 获取当前线路正在执行工程需要切割的原片
     *
     * @param workLine
     * @return
     */
    EngineeringRawQueue queryRawQueueByWorkLine(int workLine);
}

