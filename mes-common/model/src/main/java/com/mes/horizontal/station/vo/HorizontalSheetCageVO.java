package com.mes.horizontal.station.vo;

import com.mes.horizontal.details.HorizontalSheetCageDetails;
import com.mes.horizontal.station.HorizontalSheetCage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author SNG-010
 */
@ApiModel(description = "理片笼信息集合")
@Data
public class HorizontalSheetCageVO extends HorizontalSheetCage {

    @ApiModelProperty("理片笼信息集合")
    List<HorizontalSheetCageDetails> details;
}

