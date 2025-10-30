package com.mes.vertical.temprelation.dto;

import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/9/27 16:15
 * @Description:
 */
@Data
public class FlowCardVirtualSlotDTO {
    /**
     * 流程卡号
     */
    private String flowCardId;
    /**
     * 层号
     */
    private Integer layer;
    /**
     * 层号
     */
    private Integer productSortNumber;
    /**
     * /*虚拟格子
     */
    private Integer virtualSlot;
}
