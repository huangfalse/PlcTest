package com.mes.base.formula.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.hollow.formula.dto.HollowGlassFormulaDTO;

/**
 * 中空配方表(HollowFormulaDetails)表服务接口
 *
 * @author makejava
 * @since 2025-07-08 16:01:34
 */
public interface HollowFormulaDetailsService extends IService<HollowFormulaDetails> {

    /**
     * 获取料架原片详情信息
     *
     * @param glassId
     * @param flowCardId
     * @param cell
     * @return
     */
    HollowGlassFormulaDTO queryFormulaDetailsByGlassId(String glassId, String flowCardId, Integer cell);

    /**
     * 获取配方信息（分页）
     *
     * @param pageNo
     * @param pageSize
     * @param formulaName
     * @return
     */
    Page<HollowFormulaDetails> pageFormulaDetails(Integer pageNo, Integer pageSize, String formulaName);
}

