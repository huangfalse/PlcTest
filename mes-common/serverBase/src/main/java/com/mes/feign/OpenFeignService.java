package com.mes.feign;

import com.mes.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author : zhoush
 * @Date: 2025/7/14 10:19
 * @Description:
 */
@FeignClient(name = "glassStorage", contextId = "shistoryClient", path = "/rawGlassStorageHistoryTask")
public interface OpenFeignService {


    /**
     * 提出需求 - 需要原片  》》调用仓储 【上片需要玻璃】 接口
     *
     * @param stationCell 工位
     * @param width       宽
     * @param height      高
     * @param thickness   厚
     * @param filmsId     膜系
     * @param quantity    数量
     * @return 结果
     */
    @PostMapping("/getLoadGlassTask")
    Result<Boolean> getLoadGlassTask(@RequestParam Integer stationCell, @RequestParam Double width, @RequestParam Double height
            , @RequestParam Double thickness, @RequestParam String filmsId,
                                     @RequestParam Integer quantity);


    /**
     * 提出需求 - 上片工位原片需拉走替换 》》调用仓储 【退库】接口
     *
     * @param stationCell 工位
     * @param shelf       架子
     * @return 结果
     */
    @PostMapping("/resetShelf")
    Result<Boolean> resetShelf(@RequestParam("stationCell") Integer stationCell, @RequestParam("shelf") Integer shelf);
}
