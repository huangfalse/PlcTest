package com.mes.connect.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * 逻辑配置
 * @author yzx
 * @version 1.0
 */
@Slf4j
@Data
public class LogicConfig {
    private List<LogicItem> logics;

}    