package com.mes.hollow.history.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author : zhoush
 * @Date: 2025/1/3 9:36
 * @Description:
 */
@Data
public class OrderDTO {

    /**
     * <订单表>
     */
    private Integer id;
    /**
     * 订单Id
     */
    private String orderId;
    /**
     * 项目名称
     */
    private String project;
    /**
     * 客户Id
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 订单分类
     */
    private String orderClassify;
    /**
     * 商标选项
     */
    private String icon;
    /**
     * 包装方式
     */
    private String packType;
    /**
     * 送货时间
     */
    private Date deliveryDate;
    /**
     * 批次
     */
    private String batch;
    /**
     * 计算方式
     */
    private Integer calculateType;
    /**
     * 销售人员Id
     */
    private String salesmanId;
    /**
     * 销售人
     */
    private String salesman;
    /**
     * 铝条方式
     */
    private String alType;
    /**
     * 金额
     */
    private Double money;
    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 客户批次
     */
    private String customerBatch;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    private String contactNumber;
    /**
     * 送货地址
     */
    private String deliveryAddress;
    /**
     * 其他金额
     */
    private Double otherMoney;
    /**
     * 其他金额备注
     */
    private String otherMoneyRemarks;
    /**
     * 订单加工备注
     */
    private String processingNote;
    /**
     * 其他备注
     */
    private String otherRemarks;
    /**
     * 面积
     */
    private Double area;
    /**
     * 数量
     */
    private Long quantity;
    /**
     * 周长
     */
    private String perimeter;
    /**
     * 审核人id
     */
    private String verifierId;
    /**
     * 审核人
     */
    private String verifier;
    /**
     * 创建人id
     */
    private String creatorId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 下单
     */
    private Integer createOrder;
    /**
     * 工艺审核
     */
    private Integer processReview;
    /**
     * 订单审核
     */
    private Integer orderReview;
    /**
     * 生产订单
     */
    private Integer productionOrder;
    /**
     * 流程卡
     */
    private Integer processingCard;
    /**
     * 入库
     * 未入库状态0，未完全入库状态1，完全入库状态2
     */
    private Integer warehousing;
    /**
     * 发货
     */
    private Integer delivery;
    /**
     * 打印次数
     */
    private Integer printingNumber;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}
