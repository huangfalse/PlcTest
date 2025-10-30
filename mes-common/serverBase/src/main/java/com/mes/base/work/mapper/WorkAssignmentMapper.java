package com.mes.base.work.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.work.WorkAssignment;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 19:58
 * @Description:
 */
@DS("northGlassMes")
public interface WorkAssignmentMapper extends BaseMapper<WorkAssignment> {

}
