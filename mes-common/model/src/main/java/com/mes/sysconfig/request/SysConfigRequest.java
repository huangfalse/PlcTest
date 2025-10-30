package com.mes.sysconfig.request;

import com.mes.base.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/4/1 15:09
 * @Description:
 */
@ApiModel(description = ":")
@Data
public class SysConfigRequest extends PageRequest {

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(hidden = true)
    private Integer parentId;

    @ApiModelProperty(value = "配置名", position = 3)
    private String configName;

    @ApiModelProperty(value = "配置代码", position = 4)
    private String configCode;

    @ApiModelProperty(value = "配置值", position = 5)
    private String configValue;

    @ApiModelProperty(value = "是否启用", position = 6)
    private Integer isEnable;
}
