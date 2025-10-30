package com.mes.base.rawqueue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.rawqueue.vo.EngineeringRawQueueVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工程原片队列表(EngineeringRawQueue)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-09 14:18:12
 */
public interface EngineeringRawQueueMapper extends BaseMapper<EngineeringRawQueue> {


    /**
     * 按照工程号按原片队列顺序及相邻相同分组
     *
     * @param engineerId
     * @return
     */
    List<EngineeringRawQueueVO> queryRawGlassByEngineeringId(@Param("engineerId") String engineerId);


    /**
     * 按照工程及数量更新原片队列的状态
     *
     * @param engineerId
     * @param taskStateList
     * @param finishTaskState
     * @param count
     */
    void updateRawQueueByCount(@Param("engineerId") String engineerId, @Param("taskStateList") List<Integer> taskStateList, @Param("finishTaskState") int finishTaskState, @Param("count") int count);

    /**
     * 获取当前线路正在执行工程需要切割的原片
     *
     * @param workLine
     * @return
     */
    EngineeringRawQueue queryRawQueueByWorkLine(int workLine);
}

