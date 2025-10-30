package com.mes.base.engineering.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.engineering.entity.request.EngineerImportRequest;
import com.mes.base.engineering.entity.request.EngineerRequest;
import com.mes.base.engineering.entity.vo.CutDrawingInfoVO;
import com.mes.base.engineering.mapper.EngineeringMapper;
import com.mes.base.engineering.service.EngineeringService;
import com.mes.base.flowcard.service.FlowCardInfoService;
import com.mes.base.formula.service.HollowFormulaDetailsService;
import com.mes.base.glassinfo.mapper.GlassInfoMapper;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.base.rawqueue.service.EngineeringRawQueueService;
import com.mes.base.tempparameter.service.TemperingParameterService;
import com.mes.engineering.Engineering;
import com.mes.flowcard.FlowCardInfo;
import com.mes.glassinfo.GlassInfo;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.sysconst.Const;
import com.mes.tempering.param.TemperingParameter;
import com.mes.tools.EntityTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * (Engineering)表服务实现类
 *
 * @author makejava
 * @since 2025-06-02 17:48:38
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "engineering.mode", havingValue = "shuttle", matchIfMissing = false)
public class EngineeringServiceImpl extends ServiceImpl<EngineeringMapper, Engineering> implements EngineeringService {

    @Resource
    GlassInfoService glassInfoService;

    @Resource
    GlassInfoMapper glassInfoMapper;

    @Resource
    EngineeringRawQueueService engineeringRawQueueService;

    @Resource
    FlowCardInfoService flowCardInfoService;

    @Resource
    TemperingParameterService temperingParameterService;

    @Resource
    HollowFormulaDetailsService hollowFormulaDetailsService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importEngineerList(EngineerImportRequest request) {
        Engineering engineering = EntityTool.convertSingle(request, Engineering.class);
        List<GlassInfo> glassInfoList = EntityTool.batchConvert(request.getGlassInfolList(), GlassInfo.class);
        List<EngineeringRawQueue> engineeringRawQueueList = EntityTool.batchConvert(request.getEngineeringRawQueueList(), EngineeringRawQueue.class);
        List<FlowCardInfo> flowCardInfoList = EntityTool.batchConvert(request.getFlowCardInfoList(), FlowCardInfo.class);
        List<HollowFormulaDetails> hollowFormulaDetails = EntityTool.batchConvert(request.getHollowFormulaDetailsList(), HollowFormulaDetails.class);
        List<TemperingParameter> temperingParameter = EntityTool.batchConvert(request.getTemperingParameterList(), TemperingParameter.class);
        //起码保证玻璃小片数据不为null
        if (glassInfoList != null && !glassInfoList.isEmpty()) {
            this.save(engineering);
            glassInfoService.saveBatch(glassInfoList);
            engineeringRawQueueService.saveBatch(engineeringRawQueueList);
            flowCardInfoService.saveBatch(flowCardInfoList);
            hollowFormulaDetailsService.saveBatch(hollowFormulaDetails);
            temperingParameterService.saveBatch(temperingParameter);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> queryEngineerIdList(EngineerRequest request) {
        return this.listObjs(new LambdaQueryWrapper<Engineering>()
                .select(Engineering::getEngineerId)
                .eq(request.getEngineerId() != null, Engineering::getEngineerId, request.getEngineerId())
                .eq(request.getState() != 0, Engineering::getState, request.getState())
                .eq(request.getWorkLine() != 0, Engineering::getWorkLine, request.getWorkLine())).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> queryNotFinishEngineerIdList() {
        return this.listObjs(new LambdaQueryWrapper<Engineering>()
                .select(Engineering::getEngineerId)
                .ne(Engineering::getState, Const.TASK_STATE_SUCCESS)).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<EngineeringRawQueue> queryEngineeringRawQueueList(EngineerRequest request) {
        //满足条件的的工程号ID  数组
        List<String> listId = queryEngineerIdList(request);
        return engineeringRawQueueService.queryEngineeringRawQueueList(listId);
    }

    @Override
    public List<EngineeringRawQueue> queryNotFinishEngineeringRawQueueList(Integer workLine) {
        List<String> listId = this.listObjs(new LambdaQueryWrapper<Engineering>()
                .select(Engineering::getEngineerId)
                .eq(Engineering::getState, Const.TASK_STATE_START)
                .eq(Engineering::getWorkLine, workLine)).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        return engineeringRawQueueService.queryNotFinishEngineeringRawQueueList(listId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startTask(EngineerRequest request) {

        return this.update(new LambdaUpdateWrapper<Engineering>()
                .set(Engineering::getState, Const.TASK_STATE_START)
                .set(Engineering::getWorkLine, request.getWorkLine())
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .ne(Engineering::getState, Const.TASK_STATE_START)
        );
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean finishTask(EngineerRequest request) {
        return this.update(new LambdaUpdateWrapper<Engineering>()
                .set(Engineering::getState, Const.TASK_STATE_SUCCESS)
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .eq(Engineering::getWorkLine, request.getWorkLine())
        );
    }

    @Override
    public List<CutDrawingInfoVO> findCutPatten(EngineerRequest request) {
        List<CutDrawingInfoVO> list = new ArrayList<>();
        //根据前段参数找到 需要查询的工程号信息
        Engineering engineering = this.getOne(new LambdaQueryWrapper<Engineering>()
                .eq(Engineering::getEngineerId, request.getEngineerId())
                .eq(Engineering::getState, request.getState())
                .eq(Engineering::getWorkLine, request.getWorkLine()));
        //根据工程号信息查询版图信息
        if (engineering == null) {
            return list;
        }
        List<EngineeringRawQueue> upPattenUsageList = engineeringRawQueueService.queryEngineeringRawQueueList(Arrays.asList(engineering.getEngineerId()));

        // 根据工程号信息查询小片玻璃信息
        Map<Integer, List<GlassInfo>> glassInfoMap = glassInfoMapper.selectList(new QueryWrapper<GlassInfo>().lambda()
                .eq(GlassInfo::getEngineerId, engineering.getEngineerId()))
                .stream()
                .filter(e -> e.getRawSequence() != null)
                .collect(groupingBy(GlassInfo::getRawSequence));
        for (EngineeringRawQueue item : upPattenUsageList) {
            CutDrawingInfoVO cutDrawingInfoVO = new CutDrawingInfoVO();
            BeanUtils.copyProperties(item, cutDrawingInfoVO);
            cutDrawingInfoVO.setGlassInfoList(glassInfoMap.get(item.getRawSequence()));
            list.add(cutDrawingInfoVO);
        }
        return list;
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

    @Override
    public EngineerImportRequest mesImportEngineer(EngineerRequest request) {
        return null;
    }
}

