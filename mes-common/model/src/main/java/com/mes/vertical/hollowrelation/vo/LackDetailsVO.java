package com.mes.vertical.hollowrelation.vo;

import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2024/11/25 18:53
 * @Description:
 */
@Data
public class LackDetailsVO {

    /**
     * 流程卡
     */
    private String flowCardId;
    /**
     * 流程卡
     */
    private Integer layer;
    /**
     * 膜系
     */
    private String filmsId;
    /**
     * 宽度
     */
    private Double width;

    /**
     * 高度
     */
    private Double height;

    /**
     * /*厚度
     */
    private Double thickness;
    /**
     * /*总片数
     */
    private Integer totalCount;
    /**
     * /*缺少片数
     */
    private Integer lackCount;
    /**
     * /*破损片数
     */
    private Integer damageCount;


}
