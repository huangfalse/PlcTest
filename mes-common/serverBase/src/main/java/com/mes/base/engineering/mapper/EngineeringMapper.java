package com.mes.base.engineering.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.engineering.Engineering;
import com.mes.glassinfo.request.GlassInfoImportRequest;
import com.mes.rawqueue.request.EngineeringRawQueueImportRequest;

import java.util.List;

/**
 * 工程表(Engineering)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-09 14:21:56
 */
public interface EngineeringMapper extends BaseMapper<Engineering> {

    /**
     * 获取工程信息
     *
     * @param engineerId
     * @return
     */
    Engineering queryEngineeringByErp(String engineerId);

    /**
     * 获取玻璃小片信息
     *
     * @param engineerId
     * @return
     */
    List<GlassInfoImportRequest> queryGlassListByErp(String engineerId);

    /**
     * 获取原片队列信息
     *
     * @param engineerId
     * @return
     */
    List<EngineeringRawQueueImportRequest> queryRawListByErp(String engineerId);
}

