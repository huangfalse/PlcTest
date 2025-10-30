package com.mes.vertical.details.dto;

import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/11/30 13:34
 * @Description:
 */
@Data
public class FlowCardGlassInfoDTO {

    /**
     * 工程号
     */
    private String engineerId;
    /**
     * 流程卡
     */
    private String flowCardId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 膜系
     */
    private String filmsId;
    /**
     * 厚度
     */
    private Integer thickness;
    /**
     * 层号
     */
    private Integer layer;
    /**
     * 层号
     */
    private Integer sumCount;
    /**
     * 配对数量
     */
    private Integer pairCount;
    /**
     * 实际数量
     */
    private Integer realCount;
    /**
     * 破损数量
     */
    private Integer damageCount;
    /**
     * 缺少数量
     */
    private Integer lackCount;
    /**
     * 占用格子
     */
    private Integer slotCount;
}
