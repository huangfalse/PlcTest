package com.mes.connect.addressparser;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.protocol.ProtocolAddress;
import com.mes.connect.protocol.ProtocolType;


/**
 * Modbus地址解析器
 *
 * @author yzx
 * @version 1.0
 */
public class ModbusAddressParser implements AddressParser {

    /**
     * modbusTcp协议
     */
    private final String protocolMB="MB";
    /**
     * 字符串截取
     */
    private final String protocolMB_="MB.";
    /**
     * 地址类型
     */
    private final String addressTypeDB="DB";

    /**
     * 地址值类型
     */
    private final String valueTypeDBW="DBW";

    /**
     * 地址值类型
     */
    private final String valueTypeDBD="DBD";
    /**
     * 线圈
     */
    private final String functionCode0x="0x";
    /**
     * 离散输入
     */
    private final String functionCode1x="1x";
    /**
     * 离散输入
     */
    private final String functionCode3x="3x";
    /**
     * 离散输入
     */
    private final String functionCode4x="4x";
    /**
     * 离散输入
     */
    private final int functionCode3=3;
    /**
     * 读写功能码
     */
    private final int functionCode4=4;


    /**
     * @param address
     * @return
     */
    @Override
    public ProtocolAddress parse(String address) {
        // 格式示例: "MB.4x0001" 或 "MB.0x100.2"
        if (!address.startsWith(protocolMB)) {
            throw new IllegalArgumentException("Invalid Modbus address format: " + address);
        }

        String[] parts = address.substring(3).split("\\.");

        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid Modbus address format: " + address);
        }

        // 解析区域
        String areaStr = parts[0];
        int functionCode;

        if (areaStr.startsWith(functionCode0x)) {
            // 1读线圈 5写单个线圈
            functionCode = 1;
        } else if (areaStr.startsWith(functionCode1x)) {
            // 读离散输入
            functionCode = 2;
        } else if (areaStr.startsWith(functionCode3x)) {
            // 读输入寄存器
            functionCode = 4;
        } else if (areaStr.startsWith(functionCode4x)) {
            // 读保持寄存器
            functionCode = 3;
        } else {
            throw new IllegalArgumentException("Invalid Modbus area: " + areaStr);
        }

        // 解析地址
        int addressValue = Integer.parseInt(areaStr.substring(2));
        if (functionCode == functionCode3 || functionCode == functionCode4) {
            addressValue--; // Modbus保持寄存器和输入寄存器地址从0开始
        }

        // 解析位地址（如果有）
        int bit = 0;
        if (parts.length > 1) {
            bit = Integer.parseInt(parts[1]);
        }

        return new ProtocolAddress(ProtocolType.MODBUS_TCP, functionCode, 0, addressValue, bit);
    }
}    