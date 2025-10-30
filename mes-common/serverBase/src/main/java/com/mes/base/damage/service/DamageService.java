package com.mes.base.damage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.damage.Damage;
import com.mes.damage.dto.DamageDTO;
import com.mes.damage.request.DamageReportRequest;
import com.mes.damage.request.DamageRequest;
import com.mes.reportinfo.request.ReportQueryRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tao
 * @since 2024-05-30
 */
public interface DamageService extends IService<Damage> {


    /**
     * 新增破损记录
     *
     * @param damageDTO
     */
    void saveDamage(DamageDTO damageDTO);

    /**
     * 提交破损报工
     *
     * @param damageReportRequest
     */
    void submitDamage(DamageReportRequest damageReportRequest);

    /**
     * 按条件查询破损数据。
     *
     * @param reportQueryRequest 查询条件封装对象
     * @return 符合条件的破损数据列表
     */
    Page<Damage> queryDamage(ReportQueryRequest reportQueryRequest);

    /**
     * 添加批量破损数据
     *
     * @param request
     * @return
     */
    Boolean saveBatchDamage(DamageRequest request);

    /**
     * 添加批量破损数据通过流程卡
     *
     * @param request
     * @return
     */
    Boolean saveBatchDamageByFlowCardId(DamageRequest request);
}
