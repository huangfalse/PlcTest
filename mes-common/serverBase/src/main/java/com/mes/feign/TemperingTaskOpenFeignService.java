package com.mes.feign;

import com.mes.result.Result;
import com.mes.vertical.temperingtask.TemperingTask;
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
@FeignClient(name = "verticalGlass", contextId = "verticalGlass", path = "/TemperingTask")
public interface TemperingTaskOpenFeignService {

    /**
     * 保存钢化炉号队列信息
     *
     * @param list
     * @return
     */
    @PostMapping("/saveBatchTemperingTask")
    Result<Boolean> saveBatchTemperingTask(@RequestBody List<TemperingTask> list);


}

