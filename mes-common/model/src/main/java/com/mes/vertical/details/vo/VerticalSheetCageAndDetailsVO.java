package com.mes.vertical.details.vo;

import com.mes.vertical.details.VerticalSheetCageDetails;
import com.mes.vertical.station.VerticalSheetCage;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/4 17:02
 * @Description:
 */
@Data
public class VerticalSheetCageAndDetailsVO extends VerticalSheetCage {

    private List<VerticalSheetCageDetails> verticalSheetCageDetailsList;
}
