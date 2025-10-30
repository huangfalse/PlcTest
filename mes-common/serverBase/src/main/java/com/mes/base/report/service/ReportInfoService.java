package com.mes.base.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.reportinfo.ReportInfo;
import com.mes.reportinfo.dto.ReportInfoDTO;
import com.mes.reportinfo.request.ReportQueryRequest;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tao
 * @since 2024-05-30
 */
public interface ReportInfoService extends IService<ReportInfo> {

    /**
     * 保存完工数据
     *
     * @param reportInfoDTO
     */
    void saveReport(ReportInfoDTO reportInfoDTO);

    /**
     * 完工数据整合
     *
     * @param reportInfos
     * @return 报工成功与否
     */
    Boolean submitReport(List<ReportInfo> reportInfos);

    /**
     * 按条件查询完工数据
     *
     * @param reportQueryRequest
     * @return 完工数据集合
     */
    Page<ReportInfo> queryReport(ReportQueryRequest reportQueryRequest);
}
