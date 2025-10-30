package com.mes.common;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json转换工具类
 *
 * @author yzx
 * @version 1.0
 */
public class JsonConversion {
    /**
     * 使用Jackson将JSON字符串转换为实体类
     */
    public static <T> T jsonToObjectByJackson(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
