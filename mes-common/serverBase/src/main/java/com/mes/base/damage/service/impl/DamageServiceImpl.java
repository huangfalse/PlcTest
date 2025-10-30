package com.mes.base.damage.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.smallbun.screw.core.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.base.damage.mapper.DamageMapper;
import com.mes.base.damage.service.DamageService;
import com.mes.base.damagedetails.service.DamageDetailsService;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.base.work.service.WorkAssignmentService;
import com.mes.damage.Damage;
import com.mes.damage.dto.DamageDTO;
import com.mes.damage.request.DamageReportRequest;
import com.mes.damage.request.DamageRequest;
import com.mes.damagedetails.DamageDetails;
import com.mes.erp.DamageDetailsList;
import com.mes.erp.ReportingWork;
import com.mes.erp.ReportingWorkDetail;
import com.mes.feign.ErpUrlOpenFeignService;
import com.mes.glassinfo.GlassInfo;
import com.mes.reportinfo.request.ReportQueryRequest;
import com.mes.sysconst.Const;
import com.mes.work.WorkAssignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wu
 * @since 2024-06-13
 */
@Slf4j
@Service
public class DamageServiceImpl extends ServiceImpl<DamageMapper, Damage> implements DamageService {


    @Resource
    ErpUrlOpenFeignService erpUrlOpenFeignService;
    @Resource
    WorkAssignmentService workAssignmentService;
    @Resource
    DamageDetailsService damageDetailsService;
    @Resource
    GlassInfoService glassInfoService;

    /**
     * 添加报工信息
     */
    @Override
    public void saveDamage(DamageDTO damageDTO) {
        //判断破损数据是否已经存在，避免重复添加
        if (getReportExist(damageDTO)) {
            //补充破损数据中缺少的小片数据
            Damage damage = new Damage();
            BeanUtils.copyProperties(damageDTO, damage);
            LambdaQueryWrapper<GlassInfo> glassInfoSelectWrapper = new LambdaQueryWrapper<>();
            glassInfoSelectWrapper.eq(GlassInfo::getGlassId, damage.getGlassId());
            GlassInfo glassInfo = glassInfoService.getOne(glassInfoSelectWrapper);
            BeanUtils.copyProperties(glassInfo, damage);
            damage.setProductSortNumber(glassInfo.getProductSortNumber());
            damage.setCreateTime(LocalDateTime.now().toString());
            damage.setReportState(Const.REPORT_STATE_COMPLETE);
            //补充值班信息
            WorkAssignment workAssignment = workAssignmentService.getOne(
                    new LambdaQueryWrapper<WorkAssignment>()
                            .eq(WorkAssignment::getLine, damage.getLine())
                            .eq(WorkAssignment::getWorkProcess, damage.getWorkingProcedure())
            );
            if (workAssignment != null) {
                damage.setTeamsGroupsName(workAssignment.getTeamsGroupsName());
                damage.setDeviceName(workAssignment.getDeviceName());
            }
            this.save(damage);
        }
    }

    /**
     * 提交报工
     */
    @Override
    public void submitDamage(DamageReportRequest damageReportRequest) {
        List<Damage> damageList = damageReportRequest.getDamageList();
        Map<String, List<Damage>> firstMap = damageList.stream().collect(Collectors.groupingBy(e -> e.getFlowCardId() + ":"
                + e.getWorkingProcedure() + ":" + e.getDeviceName() + ":" + e.getTeamsGroupsName() + ":" + e.getProductSortNumber() + ":" + e.getLayer()));
        //保存破损明细数据
        DamageDetails newDamageDetails = new DamageDetails();
        BeanUtils.copyProperties(damageReportRequest, newDamageDetails);
        damageDetailsService.save(newDamageDetails);
        for (Map.Entry<String, List<Damage>> entry : firstMap.entrySet()) {
            ReportingWork reportingWork = new ReportingWork();
            List<ReportingWorkDetail> reportingWorkDetails = new ArrayList<>();
            List<Damage> damages = entry.getValue();
            reportingWork.setOrderId(damages.get(0).getFlowCardId().substring(0, 10));
            reportingWork.setProductionId(damages.get(0).getFlowCardId().substring(0, 11));
            reportingWork.setProcessId(damages.get(0).getFlowCardId() + "/" + damages.get(0).getLayer());
            if (StringUtils.isBlank(damages.get(0).getDeviceName())) {
                reportingWork.setDeviceName("auto");
            } else {
                reportingWork.setDeviceName(damages.get(0).getDeviceName());
            }
            //报工主表数据填充
            reportingWork.setThisProcess(damages.get(0).getWorkingProcedure());
            reportingWork.setThisCompletedQuantity(0);
            reportingWork.setThisWornQuantity(0);
            reportingWork.setClasses("早班");
            reportingWork.setReportingWorkTime(LocalDateTime.now());
            reportingWork.setTeamsGroupsName(damages.get(0).getTeamsGroupsName());
            reportingWork.setCreator("auto");

            //报工明细数据填充
            ReportingWorkDetail reportingWorkDetail = new ReportingWorkDetail();
            reportingWorkDetail.setTechnologyNumber(damages.get(0).getLayer());
            reportingWorkDetail.setOrderNumber(damages.get(0).getProductSortNumber());
            reportingWorkDetail.setCompletedQuantity(0);
            reportingWorkDetail.setBreakageQuantity(0);
            reportingWork.setThisWornQuantity(damages.size());
            reportingWorkDetail.setBreakageQuantity(damages.size());

            //破损明细数据填充
            int reportState = Const.REPORT_STATE_REPORTED;
            DamageDetailsList damageDetails = new DamageDetailsList();
            damageDetails.setBreakageQuantity(damages.size());
            damageDetails.setBreakageType(damageReportRequest.getBreakageType());
            damageDetails.setBreakageReason(damageReportRequest.getBreakageReason());
            damageDetails.setResponsibleProcess(damageReportRequest.getResponsibleProcess());
            damageDetails.setResponsibleEquipment(damageReportRequest.getResponsibleEquipment());
            damageDetails.setResponsibleTeam(damageReportRequest.getResponsibleTeam());
            damageDetails.setResponsiblePersonnel(null);
            if (Const.REPORT_STATE_PATCHED.equals(damages.get(0).getReportState())) {
                damageDetails.setQualityInspector("auto");
                damageDetails.setPatchStatus(1);
                damageDetails.setQualityInsStatus(2);
                reportState = Const.REPORT_STATE_SYNC;
            } else {
                damageDetails.setQualityInspector("");
                damageDetails.setPatchStatus(0);
                damageDetails.setQualityInsStatus(0);
            }
            reportingWorkDetail.setDamageDetails(Arrays.asList(damageDetails));
            reportingWorkDetails.add(reportingWorkDetail);
            // 创建 JSON 对象,报工数据添加到对象中
            JSONObject result = new JSONObject();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JSONArray detailsJsonArray = new JSONArray(objectMapper.writeValueAsString(reportingWorkDetails));
                result.set("detail", detailsJsonArray);
                JSONObject reportingWorkJson = new JSONObject(objectMapper.writeValueAsString(reportingWork));
                reportingWorkJson.set("reportingWorkTime", reportingWork.getReportingWorkTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                result.set("title", reportingWorkJson);
                result.set("type", 0);
                //状态为已现补的将补片状态改为1
                if (reportingWorkDetails.get(0).getDamageDetails() != null) {
                    if (reportingWorkDetails.get(0).getDamageDetails().get(0).getPatchStatus() == 1) {
                        result.set("isPatch", 1);
                    } else {
                        result.set("isPatch", 0);
                    }
                }
                result.set("userId", "admin");
                result.set("userName", "admin");
                result.set("qualityInsStatus", 0);
            } catch (Exception e) {
                log.error("JSON转换失败", e);
            }
            if (erpUrlOpenFeignService.mesSaveReportingWorkWorn(result).getData()) {
                //更新报工状态，更新关系表
                for (Damage damage : damages
                ) {
                    damage.setReportState(reportState);
                    damage.setDamageDetailsId(newDamageDetails.getId());
                }
                this.updateBatchById(damages);
            } else {
                log.info("报破损失败，玻璃信息：{}", damages);
            }
        }
    }

    @Override
    public Page<Damage> queryDamage(ReportQueryRequest reportQueryRequest) {
        return this.page(
                new Page<>(reportQueryRequest.getPageNo(), reportQueryRequest.getPageSize()),
                new LambdaQueryWrapper<Damage>()
                        .eq(Damage::getWorkingProcedure, reportQueryRequest.getWorkingProcedure())
                        .eq(Damage::getReportState, reportQueryRequest.getState())
                        .eq(!reportQueryRequest.getGlassId().isEmpty(), Damage::getGlassId, reportQueryRequest.getGlassId())
                        .between(null != reportQueryRequest.getStartTime(), Damage::getCreateTime, reportQueryRequest.getStartTime(), reportQueryRequest.getEndTime())
        );
    }

    @Override
    public Boolean saveBatchDamage(DamageRequest request) {
        for (String glassId : request.getGlassIdList()) {
            DamageDTO damageDTO = new DamageDTO();
            BeanUtils.copyProperties(request, damageDTO);
            damageDTO.setGlassId(glassId);
            saveDamage(damageDTO);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean saveBatchDamageByFlowCardId(DamageRequest request) {
        return null;
    }

    private Boolean getReportExist(DamageDTO damageDTO) {
        //判断破损数据是否已存在
        List<Damage> damages = this.list(
                new LambdaQueryWrapper<Damage>()
                        .notIn(Damage::getReportState, Const.REPORT_STATE_PATCH_ALL)
                        .lt(Damage::getReportState, Const.REPORT_STATE_PATCHED)
                        .eq(Damage::getGlassId, damageDTO.getGlassId())
        );
        if (CollectionUtils.isEmpty(damages)) {
            return Boolean.TRUE;
        } else {
            log.info("破损数据已存在，玻璃号：{}", damageDTO.getGlassId());
            return Boolean.FALSE;
        }
    }

}
