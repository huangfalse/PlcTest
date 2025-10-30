package com.mes.base.glassinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.glassinfo.mapper.GlassInfoMapper;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.glassinfo.GlassInfo;
import com.mes.glassinfo.request.NotReportRequest;
import com.mes.tempering.compute.request.ProcessCardsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wu
 * @since 2024-04-29
 */
@Service
@Slf4j
public class GlassInfoServiceImpl extends ServiceImpl<GlassInfoMapper, GlassInfo> implements GlassInfoService {

    @Override
    public boolean swapGlassInfo(GlassInfo glassSource, GlassInfo target) {
        //将源信息中待替换的玻璃信息提取
        Integer horizontal = glassSource.getIsHorizontal();
        Integer temperingLayoutId = glassSource.getTemperingLayoutId();
        Integer temperingFeedSequence = glassSource.getTemperingFeedSequence();
        Integer xCoordinate = glassSource.getXCoordinate();
        Integer yCoordinate = glassSource.getYCoordinate();
        double angle = glassSource.getAngle();
        Integer ruleId = glassSource.getRuleId();
        //将源信息中待替换的玻璃信息替换为目标玻璃
        glassSource.setIsHorizontal(target.getIsHorizontal());
        glassSource.setTemperingLayoutId(target.getTemperingLayoutId());
        glassSource.setTemperingFeedSequence(target.getTemperingFeedSequence());
        glassSource.setXCoordinate(target.getXCoordinate());
        glassSource.setYCoordinate(target.getYCoordinate());
        glassSource.setAngle(target.getAngle());
        glassSource.setRuleId(target.getRuleId());
        //将目标信息替换为源信息
        target.setIsHorizontal(horizontal);
        target.setTemperingLayoutId(temperingLayoutId);
        target.setTemperingFeedSequence(temperingFeedSequence);
        target.setXCoordinate(xCoordinate);
        target.setYCoordinate(yCoordinate);
        target.setAngle(angle);
        target.setRuleId(ruleId);
        //将信息在数据库交换
        return this.updateById(glassSource) && this.updateById(target);
    }

    @Override
    public List<GlassInfo> queryGlassListByErp(String engineerId) {
        return baseMapper.queryGlassListByErp(engineerId);
    }

    @Override
    public List<GlassInfo> queryUnreportedList(String engineerId) {
        return baseMapper.queryUnreportedList(engineerId);
    }

    @Override
    public GlassInfo queryGlassId(String glassId) {
        return this.lambdaQuery()
                .eq(GlassInfo::getGlassId, glassId)
                .one();
    }

    @Override
    public List<GlassInfo> queryNotReport(NotReportRequest request) {
        return this.baseMapper.queryNotReport(request.getEngineerId());
    }

    @Override
    public ProcessCardsRequest queryGlassListByLayer(String flowCardId, Integer layer, List<Integer> orderNumberList) {
        return baseMapper.queryGlassListByLayer(flowCardId, layer, orderNumberList);
    }
}
