package com.mes.vertical.details.vo;

import com.mes.vertical.details.dto.FlowCardGlassInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/4/9 16:26
 * @Description:
 */
@Data
public class HollowAllFlowCardVO {
    /**
     * 流程卡号
     */
    private String flowCardId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 是否直通
     */
    private Boolean isThroughSlot;
    /**
     * 已配对总数量
     */
    private Integer pairTotalCount;

    /**
     * 流程卡数据
     */
    private List<FlowCardGlassInfoDTO> flowCardGlassInfoDTOList;
}
