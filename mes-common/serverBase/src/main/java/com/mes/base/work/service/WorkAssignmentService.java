package com.mes.base.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.work.WorkAssignment;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wu
 * @since 2024-07-01
 */
public interface WorkAssignmentService extends IService<WorkAssignment> {

    /**
     * 按条件获取值班信息
     *
     * @param line
     * @param workingProcedure
     * @return 配置信息
     */
    List<WorkAssignment> selectWorkAssignment(String line, String workingProcedure);
}
