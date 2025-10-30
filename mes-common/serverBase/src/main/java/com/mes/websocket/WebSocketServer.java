package com.mes.websocket;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
/**
 * WebSocketServer class
 *
 * @author zsh
 * @date 2025/07/04
 */
@ServerEndpoint(value = "/webSocket/{webSocketName}")
@Component
public class WebSocketServer {

    /** 日志记录器，记录 WebSocketServer 类运行时日志信息 */
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /** 单次消息允许的最大字符数（超出则进行分块） */
    private static final int MAX_MESSAGE_SIZE = 50000;

    /** 用于限制并发连接数量的信号量，控制最大并发数为 100 */
    private static final Semaphore SEMAPHORE = new Semaphore(100);

    /**
     * 用户名与 WebSocket 实例映射表，支持一个用户对应多个连接。
     * 使用线程安全的 ConcurrentHashMap 存储，保证高并发下的数据一致性。
     */
    private static final Map<String, List<WebSocketServer>> SESSIONMAP = new ConcurrentHashMap<>();

    /** 当前连接对应的用户名 */
    private String webSocketName;

    /** 当前连接的 WebSocket 会话对象 */
    private Session session;

    /** 线程安全的消息缓存，用于记录当前连接收到的所有消息 */
    private final List<String> messages = new CopyOnWriteArrayList<>();


    /**
     * 连接建立
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("webSocketName") String webSocketName) {
        this.webSocketName = webSocketName;
        this.session = session;

        SESSIONMAP.computeIfAbsent(webSocketName, k -> new CopyOnWriteArrayList<>())
                .add(this);

        log.info("用户连接: webSocketName={}, 当前会话数: {}", webSocketName,
                SESSIONMAP.getOrDefault(webSocketName, Collections.emptyList()).size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        List<WebSocketServer> sessions = SESSIONMAP.get(webSocketName);
        if (sessions != null) {
            sessions.remove(this);
            if (sessions.isEmpty()) {
                SESSIONMAP.remove(webSocketName);
            }
            log.info("用户断开: webSocketName={}, 剩余会话数: {}", webSocketName,
                    SESSIONMAP.getOrDefault(webSocketName, Collections.emptyList()).size());
        }
    }

    /**
     * 接收消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息: webSocketName={}, content={}", webSocketName, message);
        JSONObject obj = JSONUtil.parseObj(message);
        // 存储消息历史
        messages.add(obj.getStr("data"));
    }

    /**
     * 错误处理
     */
    @OnError
    public void onError(Throwable error) {
        log.error("WebSocket错误: webSocketName={}", webSocketName, error);
    }

    /**
     * 向当前用户的所有会话发送消息
     */
    public void sendToWeb(String webSocketName, String message) {
        List<WebSocketServer> sessions = SESSIONMAP.get(webSocketName);
        if (sessions == null) {
            return;
        }

        sessions.forEach(ws -> {
            try {
                SEMAPHORE.acquire();
                ws.sendChunkedMessage(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("推送失败: webSocketName={}", webSocketName, e);
            } finally {
                SEMAPHORE.release();
            }
        });
    }

    /**
     * 分块发送大消息
     */
    private void sendChunkedMessage(String message) {
        if (!session.isOpen()) {
            return;
        }

        try {
            if (message.length() <= MAX_MESSAGE_SIZE) {
                session.getBasicRemote().sendText(message);
                return;
            }

            // 分块发送
            int chunks = (int) Math.ceil((double) message.length() / MAX_MESSAGE_SIZE);
            for (int i = 0; i < chunks; i++) {
                int start = i * MAX_MESSAGE_SIZE;
                int end = Math.min(start + MAX_MESSAGE_SIZE, message.length());
                String chunk = message.substring(start, end) + (i == chunks - 1 ? "<END>" : "");
                session.getBasicRemote().sendText(chunk);
            }
        } catch (IOException e) {
            log.error("消息发送失败: webSocketName={}", webSocketName, e);
        }
    }


    public static Set<String> getOnlineUsers() {
        return SESSIONMAP.keySet();
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void clearMessages() {
        messages.clear();
    }
}