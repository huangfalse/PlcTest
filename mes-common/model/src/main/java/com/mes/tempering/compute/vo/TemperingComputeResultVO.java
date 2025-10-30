package com.mes.tempering.compute.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/8 14:29
 * @Description:
 */
@Data
public class TemperingComputeResultVO implements Serializable {

    @JsonProperty("ratioResult")
    List<TemperingRatioVO> temperingRatioVOList;
    @JsonProperty("glass_details")
    List<TemperingGlassDetailsVO> temperingGlassDetailsVOList;
    @JsonProperty("guidance")
    private Integer guidance;
    @JsonProperty("rackinfos")
    private String[] rackinfos;
    @JsonProperty("resultSum")
    private Object[] resultSum;
}
