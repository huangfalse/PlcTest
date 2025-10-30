package com.mes.connect.industrialinterface;

import java.io.IOException;

/**
 * 通信统一接口 - 服务器模式
 *
 * @author yzx
 * @version 1.0
 */
public interface IndustrialServer extends AutoCloseable {
    /**
     * 开启服务
     * @throws IOException
     */
    void start() throws IOException;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 服务是否运行
     * @return
     */
    boolean isRunning();

    /**
     * 修改 handler
     * @param handler
     */
    void setDataHandler(IndustrialDataHandler handler);

    /**
     * 关闭服务
     * @throws IOException
     */
    @Override
    void close() throws IOException;
}    