package com.mes.connect.industrialinterface;

/**
 * 通信数据处理器接口
 *
 * @author yzx
 * @version 1.0
 */
public interface IndustrialDataHandler {
    /**
     * 读取地址 bit
     * @param address
     * @return
     */
    boolean handleReadBit(String address);

    /**
     * 写入地址 bit
     * @param address
     * @param value
     */
    void handleWriteBit(String address, boolean value);

    /**
     * 读取地址线圈内容
     * @param address
     * @return
     */
    int handleReadRegister(String address);

    /**
     * 写入地址线圈内容
     * @param address
     * @param value
     */
    void handleWriteRegister(String address, int value);

    /**
     * 读取地址线圈 quantity个
     * @param address
     * @param quantity
     * @return
     */
    int[] handleReadRegisters(String address, int quantity);

    /**
     * 写入地址线圈 quantity个
     * @param address
     * @param values
     */
    void handleWriteRegisters(String address, int[] values);

    /**
     * 读取地址float 类型
     * @param address
     * @return
     */
    float handleReadFloat(String address);

    /**
     * 写入地址float 类型
     * @param address
     * @param value
     */
    void handleWriteFloat(String address, float value);

    /**
     * 读取地址String 类型
     * @param address
     * @param length
     * @return
     */
    String handleReadString(String address, int length);

    /**
     * 写入地址String 类型
     * @param address
     * @param value
     */
    void handleWriteString(String address, String value);
}    