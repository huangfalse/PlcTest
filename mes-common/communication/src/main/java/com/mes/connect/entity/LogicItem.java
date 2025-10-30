package com.mes.connect.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * 逻辑配置 项
 * @author yzx
 * @version 1.0
 */
@Data
public class LogicItem {
    private String name;
    private String sequence;
    private int logicInterval;
    private ApiConfig apiConfigBefore;
    private ApiConfig apiConfigAfter;
    private List<Logic> logic;
    private List<ReturnValue> returnValue;
    @Getter
    @Setter
    private boolean equals;
}    