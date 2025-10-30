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
@ApiModel(description = ":")
@Data
public class LisecHollowGlassAndFrameDetailsDTO {

    @ApiModelProperty(hidden = true)
    private String recType;

    @ApiModelProperty(value = "项目编号(每个订单的item_num必须是唯一的 = 记录)	可以使用00001，00002...的累加排序方式	后台：按对数依次加", position = 2)
    private String itemNum;

    @ApiModelProperty(value = "识别号	自然数	后台：同步项目编号", position = 3)
    private String idNum;

    @ApiModelProperty(value = "条形码编号：仅条形码编号的最后3/4位数字（总计6/7位数字），前三位数字由批号给出	自然数	后台：条形码后三位", position = 4)
    private String barcode;

    @ApiModelProperty(value = "数量	该项目/尺寸的玻璃数量", position = 5)
    private String qty;

    @ApiModelProperty(value = "10", position = 6)
    private String width;

    @ApiModelProperty(value = "10", position = 7)
    private String height;

    @ApiModelProperty(value = "玻璃1代码	自然数", position = 8)
    private String glass1;

    @ApiModelProperty(value = "间隔版1代码	自然数	界面：间隔版对应编号字典", position = 9)
    private String frame1;

    @ApiModelProperty(value = "玻璃2代码	自然数", position = 10)
    private String glass2;

    @ApiModelProperty(value = "间隔版2代码		界面：间隔版对应编号字典", position = 11)
    private String frame2;

    @ApiModelProperty(value = "玻璃3代码", position = 12)
    private String glass3;

    @ApiModelProperty(value = "间隔版3代码		界面：间隔版对应编号字典", position = 13)
    private String frame3;

    @ApiModelProperty(value = "玻璃4代码", position = 14)
    private String glass4;

    @ApiModelProperty(value = "间隔版4代码		界面：间隔版对应编号字典", position = 15)
    private String frame4;

    @ApiModelProperty(value = "玻璃5代码", position = 16)
    private String glass5;

    @ApiModelProperty(value = "10	建议与客户协商，嵌入位与玻璃厚度及胶厚度有关，不同用户对该值有不同要求	界面：密封嵌入", position = 17)
    private String inset;

    @ApiModelProperty(value = "框架文本编号	自然数", position = 18)
    private String frameTxt;

    @ApiModelProperty(value = "气体1类型编码	若无需充气可填000000000	界面：气体类型编号字典", position = 19)
    private String gasCode1;

    @ApiModelProperty(value = "气体2类型编码		界面：气体类型编号字典", position = 20)
    private String gasCode2;

    @ApiModelProperty(value = "气体3类型编码		界面：气体类型编号字典", position = 21)
    private String gasCode3;

    @ApiModelProperty(value = "气体4类型编码		界面：气体类型编号字典", position = 22)
    private String gasCode4;

    @ApiModelProperty(value = "密封材料代码	用0填充即可", position = 23)
    private String sealType;

    @ApiModelProperty(value = "元件窗口类型编号（0固定 1通风口）	用0填充即可", position = 24)
    private String frahType;

    @ApiModelProperty(value = "10	用0填充即可", position = 25)
    private String frahHoe;

    @ApiModelProperty(value = "整个单元格窗口方向（0无图案 1水平图案 2垂直图案）	用0填充即可", position = 26)
    private String pattDir;

    @ApiModelProperty(value = "项目使用情况（0 dgu单位  1、2、3、4 LAM DGU）	用0填充即可", position = 27)
    private String dguPane;

    @ApiModelProperty(hidden = true)
    List<LisecHollowGlassDetailsDTO> glassList;

    @ApiModelProperty(hidden = true)
    List<LisecHollowFrameDetailsDTO> frameList;

}
