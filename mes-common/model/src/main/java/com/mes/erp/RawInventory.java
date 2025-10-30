package com.mes.erp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/8/7 9:50
 * @Description:
 */
@Data
public class RawInventory {

    /**
     * 宽度
     */
    @JsonProperty("width")
    private Double rawWidth;
    /**
     * 高度
     */
    @JsonProperty("height")
    private Double rawHeight;
    /**
     * 厚度
     */
    @JsonProperty("thickness")
    private Double rawThickness;
    /**
     * 膜系
     */
    @JsonProperty("model")
    private String filmsId;
    /**
     * 数量
     */
    @JsonProperty("quantity")
    private Integer remainQuantity;

    /**
     * 名称
     */
    @JsonProperty("name")
    private String manufacturer;

    /**
     * 原片编号
     */
    @JsonProperty("materialCode")
    private String rawNumber;
}

