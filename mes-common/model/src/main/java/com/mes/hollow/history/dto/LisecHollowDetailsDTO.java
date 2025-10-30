package com.mes.hollow.history.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2024/12/30 17:40
 * @Description:
 */
@ApiModel(description = "中空玻璃详情数据传输对象")
@Data
public class LisecHollowDetailsDTO {

    @ApiModelProperty(hidden = true)
    private String relRecType;

    @ApiModelProperty(value = "版本号	03.00", position = 2)
    private String relNumber;

    @ApiModelProperty(value = "其他版本信息", position = 3)
    private String relInfo;

    @ApiModelProperty(hidden = true)
    private String bthRecType;

    @ApiModelProperty(value = "批次信息", position = 5)
    private String bathInfo;

    @ApiModelProperty(value = "起始条形码（仅适用于唯一条形码）	自然数 后台：随机6位数字", position = 6)
    private String bcdStart;

    @ApiModelProperty(value = "完整批次号（后三位与文件后缀必须相同）	自然数，后三位与文件后缀必须相同 后台：随机8位数字", position = 7)
    private String batchNo;

    @ApiModelProperty(value = "", position = 8)
    private String ordRecType;

    @ApiModelProperty(value = "唯一订单号	自然数，建议参考ERP系统中与流程卡号相对应的订单号 后台：订单号NG24111491C002截取 24111491", position = 9)
    private String ord;

    @ApiModelProperty(value = "客户编号	后台：sd库 order表", position = 10)
    private String custNum;

    @ApiModelProperty(value = "客户名称	后台：sd库 order表", position = 11)
    private String custNam;

    @ApiModelProperty(value = "订单附加信息1", position = 12)
    private String text1;

    @ApiModelProperty(value = "订单附加信息2", position = 13)
    private String text2;

    @ApiModelProperty(value = "订单附加信息3", position = 14)
    private String text3;

    @ApiModelProperty(value = "订单附加信息4", position = 15)
    private String text4;

    @ApiModelProperty(value = "订单附加信息5", position = 16)
    private String text5;

    @ApiModelProperty(value = "生产日期	可以使用玻璃离开理片系统的日期 第一次报工时间", position = 17)
    private String proDate;

    @ApiModelProperty(value = "交货日期	建议参考ERP系统中订单的交货日期 sd库 order表", position = 18)
    private String delDate;

    @ApiModelProperty(value = "交货区域", position = 19)
    private String delArea;

    @ApiModelProperty(value = "中空玻璃信息", position = 20)
    List<LisecHollowGlassAndFrameDetailsDTO> glassAndFrameList;

}
