package com.mes.connect.entity;


import lombok.Data;

import java.util.Map;

/**
 * Api配置接口
 *
 * @author yzx
 * @version 1.0
 */
@Data
public class ApiConfig {
    private String codeId;
    private String type;
    private String address;
    private Map<String, Object> parameters;
}    