package com.mes.base.engineering.entity.request;

import com.mes.flowcard.FlowCardInfo;
import com.mes.flowcard.request.FlowCardInfoImportRequest;
import com.mes.glassinfo.GlassInfo;
import com.mes.glassinfo.request.GlassInfoImportRequest;
import com.mes.hollow.formula.HollowFormulaDetails;
import com.mes.hollow.formula.request.HollowFormulaDetailsImportRequest;
import com.mes.rawqueue.EngineeringRawQueue;
import com.mes.rawqueue.request.EngineeringRawQueueImportRequest;
import com.mes.tempering.param.TemperingParameter;
import com.mes.tempering.request.TemperingParameterImportRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/2 19:44
 * @Description:
 */
@Data
@ApiModel(description = "工程号信息")
public class EngineerImportRequest {

    @ApiModelProperty(value = "工程号", position = 1)
    private String engineerId;

    @ApiModelProperty(value = "工程名称", position = 2)
    private String engineerName;

    @ApiModelProperty(value = "平均利用率", position = 3)
    private String avgAvailability;

    @ApiModelProperty(value = "有效利用率", position = 4)
    private String validAvailability;

    @ApiModelProperty(value = "尾片利用率", position = 5)
    private String lastAvailability;

    @ApiModelProperty(value = "小片总数", position = 6)
    private Integer glassTotal;

    @ApiModelProperty(value = "小片总面积", position = 7)
    private Double glassTotalArea;

    @ApiModelProperty(value = "计划原片总数", position = 8)
    private Integer planPatternTotal;

    @ApiModelProperty(value = "计划原片总面积", position = 9)
    private Double planPatternTotalArea;

    @ApiModelProperty(value = "实际原片总数", position = 10)
    private Integer realityPatternTotal;

    @ApiModelProperty(value = "实际原片总面积", position = 11)
    private Double realityPatternTotalArea;

    @ApiModelProperty(value = "厚度", position = 13)
    private Double thickness;

    @ApiModelProperty(value = "膜系id", position = 12)
    private String filmsId;

    @ApiModelProperty(value = "工程原片详情", position = 14)
    private List<EngineeringRawQueueImportRequest> engineeringRawQueueList;

    @ApiModelProperty(value = "工程小片详情", position = 15)
    private List<GlassInfoImportRequest> glassInfolList;

    @ApiModelProperty(value = "工程流程卡详情", position = 16)
    private List<FlowCardInfoImportRequest> flowCardInfoList;

    @ApiModelProperty(value = "工程中空配方详情", position = 17)
    private List<HollowFormulaDetailsImportRequest> hollowFormulaDetailsList;

    @ApiModelProperty(value = "工程钢化参数详情", position = 18)
    private List<TemperingParameterImportRequest> temperingParameterList;
}

