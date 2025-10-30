package com.mes.base.engineering.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mes.base.engineering.entity.request.EngineerImportRequest;
import com.mes.base.engineering.entity.request.EngineerRequest;
import com.mes.base.engineering.service.AbstractEngineeringService;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.base.rawqueue.service.EngineeringRawQueueService;
import com.mes.engineering.Engineering;
import com.mes.flowcard.FlowCardInfo;
import com.mes.glassinfo.GlassInfo;
import com.mes.glassinfo.request.GlassInfoImportRequest;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.rawqueue.request.EngineeringRawQueueImportRequest;
import com.mes.sysconst.Const;
import com.mes.tempering.param.TemperingParameter;
import com.mes.tools.EntityTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Engineering)表服务实现类
 *
 * @author makejava
 * @since 2025-06-02 17:48:38
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "engineering.mode", havingValue = "lm", matchIfMissing = true)
public class LmEngineeringServiceImpl extends AbstractEngineeringService {

    @Resource
    GlassInfoService glassInfoService;

    @Resource
    EngineeringRawQueueService engineeringRawQueueService;

    /**
     * mes主动调用导入工程
     *
     * @param request
     * @return
     */
    @Override
    public EngineerImportRequest mesImportEngineer(EngineerRequest request) {
        EngineerImportRequest importRequest = new EngineerImportRequest();
        Engineering engineering = baseMapper.queryEngineeringByErp(request.getEngineerId());
        List<GlassInfoImportRequest> glassInfoList = baseMapper.queryGlassListByErp(request.getEngineerId());
        List<EngineeringRawQueueImportRequest> rawQueueList = baseMapper.queryRawListByErp(request.getEngineerId());
        BeanUtil.copyProperties(engineering, importRequest);
        importRequest.setGlassInfolList(glassInfoList);
        importRequest.setEngineeringRawQueueList(rawQueueList);
        return importRequest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importEngineerList(EngineerImportRequest request) {
        Engineering engineering = EntityTool.convertSingle(request, Engineering.class);
        engineering.setState(Const.TASK_STATE_NEW);
        List<GlassInfo> glassInfoList = EntityTool.batchConvert(request.getGlassInfolList(), GlassInfo.class);
        List<EngineeringRawQueue> engineeringRawQueueList = EntityTool.batchConvert(request.getEngineeringRawQueueList(), EngineeringRawQueue.class);
        engineeringRawQueueList.stream().forEach(e -> e.setState(Const.TASK_STATE_NEW));
        List<FlowCardInfo> flowCardInfoList = EntityTool.batchConvert(request.getFlowCardInfoList(), FlowCardInfo.class);
        List<HollowFormulaDetails> hollowFormulaDetails = EntityTool.batchConvert(request.getHollowFormulaDetailsList(), HollowFormulaDetails.class);
        List<TemperingParameter> temperingParameter = EntityTool.batchConvert(request.getTemperingParameterList(), TemperingParameter.class);
        //起码保证玻璃小片数据不为null
        if (CollectionUtil.isNotEmpty(glassInfoList)) {
            this.save(engineering);
            glassInfoService.saveBatch(glassInfoList);
            engineeringRawQueueService.saveBatch(engineeringRawQueueList);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> queryEngineerIdList(EngineerRequest request) {
        // 获取可执行的工程信息
        List<Engineering> engineeringList = this.list(new LambdaQueryWrapper<Engineering>().in(Engineering::getState, Const.TASK_STATE_NEW, Const.TASK_STATE_PAUSE));
        return engineeringList.stream().map(Engineering::getEngineerId).collect(Collectors.toList());
    }

    @Override
    public List<String> queryNotFinishEngineerIdList() {
        return this.listObjs(new LambdaQueryWrapper<Engineering>()
                .select(Engineering::getEngineerId)
                .ne(Engineering::getState, Const.TASK_STATE_SUCCESS)).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    /**
     * 领取的工程任务并返回原片队列
     *
     * @param request
     * @return
     */
    @Override
    public List<EngineeringRawQueue> queryEngineeringRawQueueList(EngineerRequest request) {
        //向前端返回mes原片对列表
        return engineeringRawQueueService.list(new LambdaQueryWrapper<EngineeringRawQueue>()
                .eq(EngineeringRawQueue::getEngineeringId, request.getEngineerId())
                .orderByAsc(EngineeringRawQueue::getRawSequence));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startTask(EngineerRequest request) {
        //避免任务重复发生
        boolean checkFlag = checkEngineering(request);
        if (!checkFlag) {
            return Boolean.FALSE;
        }
        //将工程状态更新
        return this.update(new LambdaUpdateWrapper<Engineering>().set(Engineering::getState, Const.TASK_STATE_START)
                .set(Engineering::getWorkLine, request.getWorkLine())
                .eq(Engineering::getEngineerId, request.getEngineerId()).in(Engineering::getState, Const.TASK_STATE_NEW, Const.TASK_STATE_PAUSE));
    }

    @Override
    public Boolean pauseTask(EngineerRequest request) {
        return this.update(new LambdaUpdateWrapper<Engineering>()
                .set(Engineering::getState, Const.TASK_STATE_PAUSE)
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .eq(Engineering::getWorkLine, request.getWorkLine())
        );
    }

    @Override
    public Boolean finishTask(EngineerRequest request) {
        return this.update(new LambdaUpdateWrapper<Engineering>()
                .set(Engineering::getState, Const.TASK_STATE_SUCCESS)
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .eq(Engineering::getWorkLine, request.getWorkLine())
        );
    }

    @Override
    public boolean checkEngineering(EngineerRequest request) {
        //避免任务重复发生
        int runningCount = this.count(new LambdaQueryWrapper<Engineering>()
                .eq(Engineering::getState, Const.TASK_STATE_START)
                .eq(Engineering::getWorkLine, request.getWorkLine()));
        if (runningCount > 0) {
            log.info("有正在执行的工程，请确保无正在执行的任务后进行操作");
            return Boolean.FALSE;
        }
        int anotherLineCount = this.count(new LambdaQueryWrapper<Engineering>()
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .ne(Engineering::getWorkLine, request.getWorkLine()));
        if (anotherLineCount > 0) {
            log.info("其他线路已领取有正在执行的工程，无法领取该工程");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

