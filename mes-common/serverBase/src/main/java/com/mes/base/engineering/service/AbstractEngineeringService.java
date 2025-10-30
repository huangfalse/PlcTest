package com.mes.base.engineering.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.engineering.entity.request.EngineerImportRequest;
import com.mes.base.engineering.entity.request.EngineerRequest;
import com.mes.base.engineering.entity.vo.CutDrawingInfoVO;
import com.mes.base.engineering.mapper.EngineeringMapper;
import com.mes.engineering.Engineering;
import com.mes.rawqueue.EngineeringRawQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * 工程表服务接口
 *
 * @author makejava
 * @since 2025-06-02 17:48:38
 */
public abstract class AbstractEngineeringService extends ServiceImpl<EngineeringMapper, Engineering> implements EngineeringService {

    /**
     * 导入工程信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean importEngineerList(EngineerImportRequest request) {
        return Boolean.FALSE;
    }


    /**
     * 获取工程列表  未领取的任务
     *
     * @param request
     * @return
     */
    @Override
    public abstract List<String> queryEngineerIdList(EngineerRequest request);

    /**
     * 领取的工程任务并返回原片队列
     *
     * @param request
     * @return
     */
    @Override
    public List<EngineeringRawQueue> queryEngineeringRawQueueList(EngineerRequest request) {
        return new ArrayList<>();
    }

    /**
     * 领取的工程任务 按线路 返回 已领取的原片队列
     *
     * @param workLine
     * @return
     */
    @Override
    public List<EngineeringRawQueue> queryNotFinishEngineeringRawQueueList(Integer workLine) {
        return new ArrayList<>();
    }

    /**
     * 开始任务
     *
     * @param request
     * @return
     */
    @Override
    public abstract Boolean startTask(EngineerRequest request);

    /**
     * 暂停任务
     *
     * @param request
     * @return
     */
    @Override
    public abstract Boolean pauseTask(EngineerRequest request);

    /**
     * 完成任务
     *
     * @param request
     * @return
     */
    @Override
    public abstract Boolean finishTask(EngineerRequest request);

    /**
     * 查询工程 切割版图信息
     *
     * @param request
     * @return
     */
    @Override
    public List<CutDrawingInfoVO> findCutPatten(EngineerRequest request) {
        return new ArrayList<>();
    }


    /**
     * 校验对应线路工程是否符合领取条件
     *
     * @param request
     * @return
     */
    @Override
    public abstract boolean checkEngineering(EngineerRequest request);

    /**
     * mes主动调用导入工程
     *
     * @param request
     * @return
     */
    @Override
    public EngineerImportRequest mesImportEngineer(EngineerRequest request) {
        return null;
    }

}

