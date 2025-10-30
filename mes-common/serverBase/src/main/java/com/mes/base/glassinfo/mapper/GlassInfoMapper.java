package com.mes.base.glassinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.glassinfo.GlassInfo;
import com.mes.tempering.compute.request.ProcessCardsRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 16:44
 * @Description:
 */
public interface GlassInfoMapper extends BaseMapper<GlassInfo> {

    /**
     * 按条件查询玻璃数据。
     *
     * @param engineerId 工程号
     * @return 玻璃信息集合
     */
    List<GlassInfo> queryGlassListByErp(@Param("engineerId") String engineerId);

    /**
     * 按条件查询玻璃数据。
     *
     * @param engineerId 工程号
     * @return 未报工玻璃信息集合
     */
    List<GlassInfo> queryUnreportedList(@Param("engineerId") String engineerId);

    /**
     * 按条件查询玻璃数据。
     *
     * @param engineerId 工程号
     * @return 未报工玻璃信息集合
     */
    List<GlassInfo> queryNotReport(@Param("engineerId") String engineerId);

    /**
     * 获取流程卡层数总信息：获取指定订单序号对应的玻璃小片信息
     *
     * @param flowCardId
     * @param layer
     * @param orderNumberList
     * @return
     */
    ProcessCardsRequest queryGlassListByLayer(@Param("flowCardId") String flowCardId, @Param("layer") Integer layer, @Param("orderNumberList") List<Integer> orderNumberList);
}
