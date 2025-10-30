package com.mes.connect.industrialinterface;


import com.mes.connect.entity.ApiConfig;
import com.mes.connect.entity.PlcParameters;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author wu
 * @since 2024-08-28
 */
public interface ApiService {

    /**
     * api参数 和PLC参数
     * @param apiConfig
     * @param plcParameters
     * @return
     */
    List<String> callApi(ApiConfig apiConfig, PlcParameters plcParameters);

    /**
     * 调用HTTP接口
     *
     * @param url  HTTP请求地址
     * @param data 请求参数
     * @return 响应结果
     */
    List<String> httpApi(String url, Map<String, Object> data);

    /**
     * 调用视图API
     *
     * @param viewName 视图名称
     * @param data     请求参数
     * @return 视图渲染结果
     */

    List<String> viewApi(String viewName, Map<String, Object> data);

    /**
     * 调用存储过程
     *
     * @param procedureName 存储过程名称
     * @param params        参数
     * @return 存储过程执行结果
     */
    List<String> procedureApi(String procedureName, Map<String, Object> params);

}
