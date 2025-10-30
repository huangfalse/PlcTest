package com.mes.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author : zhoush
 * @Date: 2025/6/27 10:01
 * @Description:
 */
@Slf4j
@Component
public class WebSocketUtils {
    @Resource
    WebSocketServer webSocketServer;
    @Resource
    ObjectMapper objectMapper;

    public <T> void sendToWeb(String webSocketName, T t) {
        try {
            String message = objectMapper.writeValueAsString(t);
            webSocketServer.sendToWeb(webSocketName, message);
        } catch (JsonProcessingException ex) {
            log.info("{}发送数据失败：{}", "rawGlassStorage", ex.getMessage());
        }
    }
}
