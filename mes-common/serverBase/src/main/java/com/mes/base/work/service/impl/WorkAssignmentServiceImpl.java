package com.mes.base.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.work.mapper.WorkAssignmentMapper;
import com.mes.base.work.service.WorkAssignmentService;
import com.mes.work.WorkAssignment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wu
 * @since 2024-07-01
 */
@Service
public class WorkAssignmentServiceImpl extends ServiceImpl<WorkAssignmentMapper, WorkAssignment> implements WorkAssignmentService {

    /**
     * 查询当前值班信息
     *
     * @param line
     * @param workingProcedure
     * @return
     */
    @Override
    public List<WorkAssignment> selectWorkAssignment(String line, String workingProcedure) {
        LambdaQueryWrapper<WorkAssignment> workAssignmentSelectWrapper = new LambdaQueryWrapper<>();
        workAssignmentSelectWrapper
                .eq(WorkAssignment::getLine, line)
                .eq(WorkAssignment::getWorkProcess, workingProcedure);
        return baseMapper.selectList(workAssignmentSelectWrapper);
    }
}
