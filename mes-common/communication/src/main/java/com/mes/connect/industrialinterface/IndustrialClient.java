package com.mes.connect.industrialinterface;

import java.io.IOException;


/**
 * 通信统一接口 - 客户端模式
 *
 * @author yzx
 * @version 1.0
 */
public interface IndustrialClient extends AutoCloseable {
    /**
     * 开启连接
     * @throws IOException
     */
    void connect() throws IOException;

    /**
     * 关闭连接
     */
    void disconnect();

    /**
     * 当前连接状态
     * @return
     */
    boolean isConnected();

    /**
     * 数据读写方法
     * @param address
     * @return
     * @throws IOException
     */
    boolean readBit(String address) throws IOException;

    /**
     * 读bit 位
     * @param address
     * @param value
     * @throws IOException
     */
    void writeBit(String address, boolean value) throws IOException;

    /**
     * 读取地址线圈
     * @param address
     * @return
     * @throws IOException
     */
    int readRegister(String address) throws IOException;

    /**
     * 写入地址线圈
     * @param address
     * @param value
     * @throws IOException
     */
    void writeRegister(String address, int value) throws IOException;

    /**
     * 读取地址 线圈 quantity个
     * @param address
     * @param quantity
     * @return
     * @throws IOException
     */
    int[] readRegisters(String address, int quantity) throws IOException;

    /**
     * 写入地址 线圈 quantity个
     * @param address
     * @param values
     * @throws IOException
     */
    void writeRegisters(String address, int[] values) throws IOException;

    /**
     * 读取地址 float类型
     * @param address
     * @return
     * @throws IOException
     */
    float readFloat(String address) throws IOException;

    /**
     * 写入地址 float类型
     * @param address
     * @param value
     * @throws IOException
     */
    void writeFloat(String address, float value) throws IOException;

    /**
     * 读取地址 String 类型
     * @param address
     * @param length
     * @return
     * @throws IOException
     */
    String readString(String address, int length) throws IOException;

    /**
     * 写入地址 String 类型
     * @param address
     * @param value
     * @throws IOException
     */
    void writeString(String address, String value) throws IOException;

    /**
     * 关闭连接
     * @throws IOException
     */
    @Override
    void close() throws IOException;
}    