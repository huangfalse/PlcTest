package com.mes.base.rawqueue.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.rawqueue.mapper.EngineeringRawQueueMapper;
import com.mes.base.rawqueue.service.EngineeringRawQueueService;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.rawqueue.request.EngineeringRawQueueRequest;
import com.mes.rawqueue.vo.EngineeringRawQueueVO;
import com.mes.sysconst.Const;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工程原片队列表(EngineeringRawQueue)表服务实现类
 *
 * @author makejava
 * @since 2025-07-09 14:18:12
 */
@Service
public class EngineeringRawQueueServiceImpl extends ServiceImpl<EngineeringRawQueueMapper, EngineeringRawQueue> implements EngineeringRawQueueService {

    @Override
    public List<EngineeringRawQueue> queryEngineeringRawQueueList(List<String> listEngineeringId) {
        if (listEngineeringId.isEmpty()) {
            return new ArrayList<EngineeringRawQueue>();
        }
        return this.list(new QueryWrapper<EngineeringRawQueue>().lambda()
                .in(EngineeringRawQueue::getEngineeringId, listEngineeringId));
    }

    @Override
    public List<EngineeringRawQueue> queryNotFinishEngineeringRawQueueList(List<String> listEngineeringId) {
        if (listEngineeringId.isEmpty()) {
            return new ArrayList<EngineeringRawQueue>();
        }
        return this.list(new QueryWrapper<EngineeringRawQueue>().lambda()
                .in(EngineeringRawQueue::getEngineeringId, listEngineeringId)
                .lt(EngineeringRawQueue::getState, Const.TASK_STATE_SUCCESS));
    }

    @Override
    public boolean updateEngineeringRawQueue(EngineeringRawQueueRequest request) {
        return this.update(new UpdateWrapper<EngineeringRawQueue>().lambda()
                .set(EngineeringRawQueue::getState, request.getState())
                .eq(EngineeringRawQueue::getId, request.getId()));
    }

    @Override
    public List<EngineeringRawQueue> queryRawQueueList(String engineerId) {
        //将工程下的原片按顺序将相邻尺寸相同的排序 不同重新排序
        List<EngineeringRawQueueVO> rawQueueVOList = baseMapper.queryRawGlassByEngineeringId(engineerId);
        if (CollectionUtil.isEmpty(rawQueueVOList)) {
            return new ArrayList<>();
        }
        //将原片队列分组并获取第一组尺寸相同的原片列表最多取两片
        Map<String, List<EngineeringRawQueue>> upListMap = rawQueueVOList.stream()
                .collect(Collectors.groupingBy(EngineeringRawQueueVO::getGroupNumber, Collectors.toList()));
        List<EngineeringRawQueue> tempRawQueueVOList = upListMap.get("1");
        return tempRawQueueVOList.stream().limit(2).collect(Collectors.toList());
    }

    @Override
    public void updateRawQueueByCount(String engineerId, List<Integer> taskStateList, Integer finishTaskState, int count) {
        baseMapper.updateRawQueueByCount(engineerId, taskStateList, finishTaskState, count);
    }

    @Override
    public EngineeringRawQueue queryRawQueueByWorkLine(int workLine) {
        return baseMapper.queryRawQueueByWorkLine(workLine);
    }
}

