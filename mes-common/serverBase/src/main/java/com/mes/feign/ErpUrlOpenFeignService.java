package com.mes.feign;

import com.mes.erp.RawInventory;
import com.mes.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * The interface Erp url open feign service.
 *
 * @Author : zhoush
 * @Date: 2025 /7/7 11:25
 * @Description:
 */
@FeignClient(name = "erp-service", url = "${erp.url}")
public interface ErpUrlOpenFeignService {
    /**
     * 破损报工
     *
     * @param reportingWork the reporting work
     * @return result
     * @
     */
    @PostMapping("/reportingWork/mesSaveReportingWorkWorn")
    Result<Boolean> mesSaveReportingWorkWorn(@RequestBody Map<String, Object> reportingWork);

    /**
     * 完工报工
     *
     * @param reportingWork the reporting work
     * @return result
     */
    @PostMapping("/reportingWork/mesReportingWork")
    Result<Boolean> mesReportingWork(@RequestBody Map<String, Object> reportingWork);

    /**
     * Mes切割原片报工
     *
     * @param reportingWork the reporting work
     * @return result
     */
    @PostMapping("/materialInventory/mesMaterialOutbound")
    Result<Boolean> mesMaterialOutbound(@RequestBody Map<String, Object> reportingWork);

    /**
     * Mes原片获取
     *
     * @return result
     */
    @PostMapping("/materialInventory/mesOriginalSelect")
    Result<List<RawInventory>> mesOriginalSelect();


}

