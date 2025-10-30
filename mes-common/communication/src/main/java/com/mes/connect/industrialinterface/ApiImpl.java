package com.mes.connect.industrialinterface;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.connect.entity.ApiConfig;
import com.mes.connect.entity.PlcParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Api 实现
 *
 * @author yzx
 * @version 1.0
 */
@Slf4j
@Service
public class ApiImpl implements ApiService {


    private final RestTemplate restTemplate;

    private final JdbcTemplate jdbcTemplate;
    private String methodPost="POST";
    private String methodGet="GET";
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /**
     * 使用构造函数注入
     * @param restTemplate
     * @param jdbcTemplate
     */
    @Autowired
    public ApiImpl(RestTemplate restTemplate, JdbcTemplate jdbcTemplate) {
        this.restTemplate = restTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 发送调用接口请求 ，根据逻辑配置调用
     *
     * @param apiConfig     逻辑配置参数
     * @param plcParameters plc参数
     * @return 响应内容按行分割的数组
     */
    @Override
    public List<String> callApi(ApiConfig apiConfig, PlcParameters plcParameters) {
        try {
            List<String> result = new ArrayList<>();
            String connectType = apiConfig.getType();
            String connectAddress = apiConfig.getAddress();
            Map<String, Object> map = new HashMap<String, Object>(1);
            map.put("apiConfig", apiConfig);
            map.put("plcParameter", plcParameters);
            switch (connectType) {
                case "Http":
                // http接口
                    result = this.httpApi(connectAddress, map);
                    break;
                case "View":
                // 视图/表
                    result = this.viewApi(connectAddress, map);
                    break;
                case "Procedure":
                // 存储过程
                    result = this.procedureApi(connectAddress, map);
                    break;
                default:
                // 不支持的方式
                    log.warn("不支持的连接类型: {}", connectType);
                    return null;
            }
            return result;
        } catch (Exception e) {
            log.error("调用接口失败: {}", e.getMessage(), e);
        }
        return null;
    }


    /**
     * 发送HTTP请求，支持GET和POST方法
     *
     * @param url  请求URL
     * @param data 请求参数或请求体
     * @return 响应内容按行分割的数组
     */
    @DS("mes_machine")
    @Override
    public List<String> httpApi(String url, Map<String, Object> data) {
        try {
            // 构建URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            // 处理响应
            String responseBody;
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> apiConfig = mapper.convertValue(data.get("apiConfig"), Map.class);
            Map<String, Object> parameters = mapper.convertValue(apiConfig.get("parameters"), Map.class);
            String method = parameters.get("method").toString();
            data.remove("parameters");
            if (methodGet.equals(method)) {
                // GET请求：将参数添加到URL查询参数中
                if (data != null) {
                    data.forEach(builder::queryParam);
                }
                // 发送GET请求
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        String.class
                );
                responseBody = response.getBody();
            } else if (methodPost.equals(method)) {
                // POST请求：将参数作为请求体
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<?> entity = new HttpEntity<>(data, headers);
                // 发送POST请求
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                responseBody = response.getBody();
            } else {
                throw new IllegalArgumentException("不支持的HTTP方法: " + method);
            }
            // 直接提取data数组
            List<String> dataList = JSON.parseObject(responseBody)
                    .getJSONArray("data")
                    .toJavaList(String.class);
            return dataList;
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
            return null;
        }
    }

    @DS("mes_machine")
    @Override
    public List<String> viewApi(String viewName, Map<String, Object> parameters) {
        // 验证视图名是否合法，防止SQL注入
        if (!isValidViewName(viewName)) {
            throw new IllegalArgumentException("无效的视图名称");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> apiConfig = mapper.convertValue(parameters.get("apiConfig"), Map.class);
        Map<String, Object> params = mapper.convertValue(apiConfig.get("parameters"), Map.class);
        // 使用预编译语句构建查询
        StringBuilder sql = new StringBuilder("SELECT * FROM " + viewName);
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        if (params != null && !params.isEmpty()) {
            sql.append(" WHERE ");
            boolean first = true;

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!first) {
                    sql.append(" AND ");
                }
                sql.append(entry.getKey()).append(" = :").append(entry.getKey());
                paramSource.addValue(entry.getKey(), entry.getValue());
                first = false;
            }
        }
        // 使用Map参数执行查询并转换结果
        List<Map<String, Object>> resultList = namedParameterJdbcTemplate.queryForList(
                sql.toString(),
                paramSource.getValues()
        );

        return convertResultToList(resultList);
    }

    @DS("jiumumes")
    @Override
    public List<String> procedureApi(String procedureName, Map<String, Object> params) {
        try {
            if (!isValidProcedureName(procedureName)) {
                throw new IllegalArgumentException("无效的存储过程名称");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> apiConfig = mapper.convertValue(params.get("apiConfig"), Map.class);
            Map<String, Object> parameters = mapper.convertValue(apiConfig.get("parameters"), Map.class);
            Map<String, Object> inParams = mapper.convertValue(parameters.get("inParams"), Map.class);
            Map<String, Object> outParams = mapper.convertValue(parameters.get("outParams"), Map.class);
            // 创建新的 Map 并合并
            Map<String, Object> mergedMap = new HashMap<>(inParams);
            mergedMap.putAll(outParams);
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName(procedureName)
                    .withoutProcedureColumnMetaDataAccess();
            for (Map.Entry<String, Object> entry : inParams.entrySet()) {
                int sqlType = getSqlType(entry.getValue());
                // 作为输入参数
                jdbcCall.declareParameters(
                        new SqlParameter(entry.getKey(), sqlType)
                );
            }
            for (Map.Entry<String, Object> entry : outParams.entrySet()) {
                int outSqlType = 12;
                // 使用指定的SQL类型作为输出参数
                jdbcCall.declareParameters(
                        new SqlOutParameter(entry.getKey(), outSqlType)
                );
            }
            // 执行存储过程并获取结果
            Map<String, Object> result = jdbcCall.execute(mergedMap);

            // 处理输出参数
            if (outParams != null) {
                for (String paramName : outParams.keySet()) {
                    if (result.containsKey(paramName)) {
                        // 将输出参数的值放回原参数Map中
                        outParams.put(paramName, result.get(paramName));
                    }
                }
            }
            List<String> outParamsValues = outParams.values().stream()
                    .map(value -> value != null ? value.toString() : "null")
                    .collect(Collectors.toList());
            // 返回结果信息
            return outParamsValues;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将查询结果转换为字符串数组
     * @param resultList
     * @return
     */
    private List<String> convertResultToList(List<Map<String, Object>> resultList) {
        List<String> resultStrings = new ArrayList<>();
        for (Map<String, Object> row : resultList) {
            for (String key : row.keySet()) {
                resultStrings.add(row.get(key).toString());
            }
            return resultStrings;
        }
        return resultStrings;
    }

    /**
     * 类型映射方法
     * @param value
     * @return
     */
    private int getSqlType(Object value) {
        if (value instanceof String) {return Types.VARCHAR;}
        if (value instanceof Integer) {return Types.INTEGER;}
        if (value instanceof Double) {return Types.DOUBLE;}
        if (value instanceof java.util.Date) {return Types.TIMESTAMP;}
        if (value instanceof Boolean) {return Types.BOOLEAN;}
        return Types.VARCHAR;
    }

    /**
     * 验证视图名称（防止SQL注入）
     * @param viewName
     * @return
     */
    private boolean isValidViewName(String viewName) {
        // 简单验证：只允许字母、数字和下划线，且长度不超过50
        return viewName.matches("^[a-zA-Z0-9_]{1,50}$");
    }

    /**
     * 验证存储过程名称
     * @param procedureName
     * @return
     */
    private boolean isValidProcedureName(String procedureName) {
        return procedureName.matches("^[a-zA-Z0-9_]{1,50}$");
    }

}
