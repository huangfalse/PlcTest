package com.mes.vertical.details.vo;

import com.mes.glassinfo.GlassInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author : zhoush
 * @Date: 2025/6/30 11:21
 * @Description:
 */
@ApiModel(description = "缺片详情")
public class TemperingGlassLackVO extends GlassInfo {


    @ApiModelProperty(value = "是否破损", position = 1)
    private Integer isDamage;

    /**
     * 重写toString方法，包含父类属性和当前类属性
     * 便于日志打印和问题排查
     * @return 包含所有属性的字符串表示
     */
    @Override
    public String toString() {
        return "TemperingGlassLackVO{" +
                "isDamage=" + isDamage +
                ", " + super.toString() +
                '}';
    }
}
