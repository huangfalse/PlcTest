package com.mes.base.engineering.entity.vo;

import com.mes.glassinfo.GlassInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/6/17 10:22
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class CutDrawingInfoVO {


    @ApiModelProperty(value = "原片版图小片信息", position = 1)
    private List<GlassInfo> glassInfoList;

    @ApiModelProperty(value = "原片工程号", position = 2)
    private String engineeringId;

    @ApiModelProperty(value = "原片片序", position = 3)
    private Integer layoutSequence;

    @ApiModelProperty(value = "总片序", position = 4)
    private Integer totalRawSequence;

    @ApiModelProperty(value = "宽", position = 5)
    private Double width;

    @ApiModelProperty(value = "高", position = 6)
    private Double height;

}
