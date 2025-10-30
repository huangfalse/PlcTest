package com.mes.base.report.service.impl;

import cn.smallbun.screw.core.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.base.damage.service.DamageService;
import com.mes.base.glassinfo.service.GlassInfoService;
import com.mes.base.report.mapper.ReportInfoMapper;
import com.mes.base.report.service.ReportInfoService;
import com.mes.base.work.service.WorkAssignmentService;
import com.mes.damage.Damage;
import com.mes.erp.ReportingWork;
import com.mes.erp.ReportingWorkDetail;
import com.mes.feign.ErpUrlOpenFeignService;
import com.mes.feign.OpenFeignService;
import com.mes.glassinfo.GlassInfo;
import com.mes.reportinfo.ReportInfo;
import com.mes.reportinfo.dto.ReportInfoDTO;
import com.mes.reportinfo.request.ReportQueryRequest;
import com.mes.sysconst.Const;
import com.mes.work.WorkAssignment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
public class ReportInfoServiceImpl extends ServiceImpl<ReportInfoMapper, ReportInfo> implements ReportInfoService {

    @Resource
    ErpUrlOpenFeignService erpUrlOpenFeignService;
    @Resource
    WorkAssignmentService workAssignmentService;
    @Resource
    GlassInfoService glassInfoService;
    @Resource
    DamageService damageService;
    @Resource
    OpenFeignService openFeignService;

    @Override
    public void saveReport(ReportInfoDTO reportInfoDTO) {
        //判断完工数据是否已经存在，避免重复添加
        if (getReportExist(reportInfoDTO)) {
            //补充破损记录缺少的小片数据
            ReportInfo reportInfo = new ReportInfo();
            BeanUtils.copyProperties(reportInfoDTO, reportInfo);
            LambdaQueryWrapper<GlassInfo> glassInfoSelectWrapper = new LambdaQueryWrapper<>();
            glassInfoSelectWrapper.eq(GlassInfo::getGlassId, reportInfo.getGlassId());
            GlassInfo glassInfo = glassInfoService.getOne(glassInfoSelectWrapper);
            BeanUtils.copyProperties(glassInfo, reportInfo);
            reportInfo.setProductSortNumber(glassInfo.getProductSortNumber());
            reportInfo.setCreateTime(LocalDateTime.now().toString());
            //补充值班信息
            WorkAssignment workAssignment = workAssignmentService.getOne(
                    new LambdaQueryWrapper<WorkAssignment>()
                            .eq(WorkAssignment::getLine, reportInfo.getLine())
                            .eq(WorkAssignment::getWorkProcess, reportInfo.getWorkingProcedure())
            );
            if (workAssignment != null) {
                reportInfo.setTeamsGroupsName(workAssignment.getTeamsGroupsName());
                reportInfo.setDeviceName(workAssignment.getDeviceName());
            }
            //进行报工,如果报工成功，则将报工状态设置为已报工，否则设置未报工
            if (submitReport(Arrays.asList(reportInfo))) {
                reportInfo.setReportState(Const.REPORT_STATE_REPORTED);
            } else {
                reportInfo.setReportState(Const.REPORT_STATE_COMPLETE);
            }

            //删除未现补未报工的破损拿走记录
            damageService.remove(
                    new LambdaQueryWrapper<Damage>()
                            .eq(Damage::getGlassId, reportInfo.getGlassId())
                            .lt(Damage::getReportState, Const.REPORT_STATE_REPORTED)
            );
            this.save(reportInfo);
        }
    }

    @Override
    public Boolean submitReport(List<ReportInfo> reportInfos) {
        for (ReportInfo reportInfo : reportInfos
        ) {
            ReportingWork reportingWork = new ReportingWork();
            reportingWork.setOrderId(reportInfo.getFlowCardId().substring(0, 10));
            reportingWork.setProductionId(reportInfo.getFlowCardId().substring(0, 11));
            reportingWork.setProcessId(reportInfo.getFlowCardId() + "/" + reportInfo.getLayer());
            if (StringUtils.isBlank(reportInfo.getDeviceName())) {
                reportingWork.setDeviceName("auto");
            } else {
                reportingWork.setDeviceName(reportInfo.getDeviceName());
            }
            reportingWork.setThisProcess(reportInfo.getWorkingProcedure());
            reportingWork.setThisCompletedQuantity(1);
            reportingWork.setThisWornQuantity(0);
            reportingWork.setClasses("早班");
            reportingWork.setReportingWorkTime(LocalDateTime.now());
            reportingWork.setTeamsGroupsName(reportInfo.getTeamsGroupsName());
            reportingWork.setCreator("auto");

            List<ReportingWorkDetail> reportingWorkDetails = new ArrayList<>();
            ReportingWorkDetail reportingWorkDetail = new ReportingWorkDetail();
            reportingWorkDetail.setTechnologyNumber(reportInfo.getLayer());
            reportingWorkDetail.setOrderNumber(reportInfo.getProductSortNumber());
            reportingWorkDetail.setCompletedQuantity(1);
            reportingWorkDetail.setBreakageQuantity(0);
            reportingWorkDetails.add(reportingWorkDetail);
            // 创建 JSON 对象,报工数据添加到对象中

            Map<String, Object> result = new HashMap<>(16);
            try {

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> detailsMap = objectMapper.convertValue(reportingWorkDetails.get(0), new TypeReference<Map<String, Object>>() {
                });
                result.put("detail", detailsMap);
                reportingWork.setProcessId(reportingWork.getProcessId().substring(0, 14));
                Map<String, Object> reportingWorkMap = objectMapper.convertValue(reportingWork, new TypeReference<Map<String, Object>>() {
                });
                reportingWorkMap.put("reportingWorkTime", reportingWork.getReportingWorkTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                result.put("title", reportingWorkMap);
                result.put("type", 0);
                if (reportingWorkDetails.get(0).getDamageDetails() != null) {
                    int patchStatus = reportingWorkDetails.get(0).getDamageDetails().get(0).getPatchStatus();
                    result.put("isPatch", patchStatus == 1 ? 1 : 0);
                }
                result.put("userId", "admin");
                result.put("userName", "admin");
                result.put("qualityInsStatus", 0);
            } catch (Exception e) {
                log.error("JSON转换失败", e);
            }
            if (erpUrlOpenFeignService.mesReportingWork(result).getData()) {
                reportInfo.setReportState(Const.REPORT_STATE_REPORTED);
            } else {
                log.info("报完工失败，玻璃信息：{}", reportInfo);
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Page<ReportInfo> queryReport(ReportQueryRequest reportQueryRequest) {
        return this.page(
                new Page<>(reportQueryRequest.getPageNo(), reportQueryRequest.getPageSize()),
                new LambdaQueryWrapper<ReportInfo>()
                        .eq(ReportInfo::getWorkingProcedure, reportQueryRequest.getWorkingProcedure())
                        .eq(ReportInfo::getReportState, reportQueryRequest.getState())
                        .eq(!reportQueryRequest.getGlassId().isEmpty(), ReportInfo::getGlassId, reportQueryRequest.getGlassId())
                        .between(null != reportQueryRequest.getStartTime(), ReportInfo::getCreateTime, reportQueryRequest.getStartTime(), reportQueryRequest.getEndTime())
        );
    }

    private Boolean getReportExist(ReportInfoDTO reportInfoDTO) {
        //判断完工数据是否已存在
        List<ReportInfo> reportInfos = this.list(
                new LambdaQueryWrapper<ReportInfo>()
                        .eq(ReportInfo::getGlassId, reportInfoDTO.getGlassId())
                        .eq(ReportInfo::getLine, reportInfoDTO.getLine())
                        .eq(ReportInfo::getWorkingProcedure, reportInfoDTO.getWorkingProcedure())
        );
        if (CollectionUtils.isEmpty(reportInfos)) {
            return Boolean.TRUE;
        } else {
            log.info("完工数据已存在，玻璃号：{}", reportInfoDTO.getGlassId());
            return Boolean.FALSE;
        }
    }
}
