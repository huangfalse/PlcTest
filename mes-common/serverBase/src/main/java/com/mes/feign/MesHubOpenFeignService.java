package com.mes.feign;

import com.mes.result.Result;
import com.mes.tempering.compute.request.TemperingComputeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The interface Erp url open feign service.
 *
 * @Author : zhoush
 * @Date: 2025 /7/7 11:25
 * @Description:
 */
@FeignClient(name = "mesHub-service", url = "http://127.0.0.1:8079")
public interface MesHubOpenFeignService {
    /**
     * 模拟计算接口
     *
     * @param request the reporting work
     * @return result
     * @
     */
    @PostMapping("/computeTempering/inputTemperingData")
    Result<String> inputTemperingData(@RequestBody TemperingComputeRequest request);


}

