package com.mes.load.station.entity.vo;

import com.mes.load.details.LoadStationDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 上片工位表(LoadStation)表实体类
 *
 * @author makejava
 * @since 2025-07-08 16:02:32
 */
@ApiModel(description = "上片工位数据请求")
@Data
public class LoadStationInfoVo {

    @ApiModelProperty(value ="id",position = 1)
    private Long id;

    @ApiModelProperty(value ="设备id",position = 2)
    private Integer deviceId;

    @ApiModelProperty(value ="栅格号",position = 3)
    private Integer slot;

    @ApiModelProperty(value ="启用/禁用",position = 4)
    private Integer enableState;

    @ApiModelProperty(value ="工位原片信息",position = 5)
    private List<LoadStationDetails> listLoadStationDetails;


}

