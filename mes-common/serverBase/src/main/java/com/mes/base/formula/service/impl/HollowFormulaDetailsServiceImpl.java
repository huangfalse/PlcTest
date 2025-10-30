package com.mes.base.formula.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.formula.mapper.HollowFormulaDetailsMapper;
import com.mes.base.formula.service.HollowFormulaDetailsService;
import com.mes.hollow.formula.dto.HollowGlassFormulaDTO;
import com.mes.hollow.formula.HollowFormulaDetails;
import org.springframework.stereotype.Service;

/**
 * 中空配方表(HollowFormulaDetails)表服务实现类
 *
 * @author makejava
 * @since 2025-07-08 16:01:34
 */
@Service("hollowFormulaDetailsService")
public class HollowFormulaDetailsServiceImpl extends ServiceImpl<HollowFormulaDetailsMapper, HollowFormulaDetails> implements HollowFormulaDetailsService {

    @Override
    public HollowGlassFormulaDTO queryFormulaDetailsByGlassId(String glassId, String flowCardId, Integer cell) {
        return baseMapper.queryFormulaDetailsByGlassId(glassId, flowCardId, cell);
    }

    @Override
    public Page<HollowFormulaDetails> pageFormulaDetails(Integer pageNo, Integer pageSize, String formulaName) {
        Page<HollowFormulaDetails> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<HollowFormulaDetails> wrapper = new LambdaQueryWrapper<HollowFormulaDetails>().like(StringUtils.isNotBlank(formulaName)
                , HollowFormulaDetails::getFormulaName, formulaName);
        return this.page(page, wrapper);
    }

}

