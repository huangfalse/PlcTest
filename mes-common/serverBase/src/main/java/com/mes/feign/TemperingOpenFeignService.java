package com.mes.feign;

import com.mes.result.Result;
import com.mes.tempering.queue.TemperingGlassQueueInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * The interface Erp url open feign service.
 *
 * @Author : zhoush
 * @Date: 2025 /7/7 11:25
 * @Description:
 */
@FeignClient(name = "temperingGlass", contextId = "temperingGlass", path = "/temperingGlassQueueInfo")
public interface TemperingOpenFeignService {

    /**
     * 保存钢化队列信息
     *
     * @param temperingGlassQueueInfoList
     * @return
     */
    @PostMapping("/saveTempingQueue")
    Result<Boolean> saveTempingQueue(@RequestBody List<TemperingGlassQueueInfo> temperingGlassQueueInfoList);


}

