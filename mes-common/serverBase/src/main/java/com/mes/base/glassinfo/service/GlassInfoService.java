package com.mes.base.glassinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.glassinfo.GlassInfo;
import com.mes.glassinfo.request.NotReportRequest;
import com.mes.tempering.compute.request.ProcessCardsRequest;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wu
 * @since 2024-04-29
 */
public interface GlassInfoService extends IService<GlassInfo> {

    /**
     * 将源玻璃信息与目标目标信息替换：仅替换钢化信息
     *
     * @param glassSource 源玻璃信息
     * @param glassTarget 目标玻璃信息
     * @return
     */
    boolean swapGlassInfo(GlassInfo glassSource, GlassInfo glassTarget);

    /**
     * 按条件查询玻璃数据。
     *
     * @param engineerId 工程号
     * @return 玻璃信息集合
     */
    List<GlassInfo> queryGlassListByErp(String engineerId);

    /**
     * 按条件查询玻璃数据。
     *
     * @param engineerId 工程号
     * @return 未报工玻璃信息集合
     */
    List<GlassInfo> queryUnreportedList(String engineerId);

    /**
     * 按玻璃id查询玻璃数据。
     *
     * @param glassId 玻璃id
     * @return 小片信息
     */
    GlassInfo queryGlassId(String glassId);

    /**
     * 根据工程号 查询未破损 且未完工的玻璃信息
     *
     * @param request 工程号
     * @return 完工数据集合
     */
    List<GlassInfo> queryNotReport(NotReportRequest request);


    /**
     * 获取流程卡层数总信息
     *
     * @param flowCardId
     * @param layer
     * @param orderNumberList
     * @return
     */
    ProcessCardsRequest queryGlassListByLayer(String flowCardId, Integer layer, List<Integer> orderNumberList);
}

