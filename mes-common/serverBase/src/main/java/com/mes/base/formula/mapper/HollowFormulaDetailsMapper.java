package com.mes.base.formula.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.hollow.formula.dto.HollowGlassFormulaDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 中空配方表(HollowFormulaDetails)表数据库访问层
 *
 * @author makejava
 * @since 2025-07-08 16:01:34
 */
public interface HollowFormulaDetailsMapper extends BaseMapper<HollowFormulaDetails> {

    /**
     * 获取料架原片详情信息
     *
     * @param glassId
     * @param flowCardId
     * @param cell
     * @return
     */
    HollowGlassFormulaDTO queryFormulaDetailsByGlassId(@Param("glassId") String glassId, @Param("flowCardId") String flowCardId, @Param("cell") Integer cell);

}

