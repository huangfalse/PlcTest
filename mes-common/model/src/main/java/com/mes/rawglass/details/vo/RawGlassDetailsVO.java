package com.mes.rawglass.details.vo;

import com.mes.rawglass.details.RawGlassStorageDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/14 20:32
 * @Description:
 */
@ApiModel(description = "料架信息及详情")
@Data
public class RawGlassDetailsVO {

    @ApiModelProperty(value = "设备id", position = 2)
    private Integer deviceId;

    @ApiModelProperty(value = "格子id", position = 3)
    private Integer slot;

    @ApiModelProperty(value = "架子号：分为单双数", position = 4)
    private Integer shelf;

    @ApiModelProperty(value = "启用/禁用", position = 5)
    private Integer enableState;

    @ApiModelProperty(value = "料架详情", position = 6)
    List<RawGlassStorageDetails> rawGlassDetailsList;
}
