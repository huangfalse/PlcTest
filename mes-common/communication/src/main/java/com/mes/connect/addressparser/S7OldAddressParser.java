package com.mes.connect.addressparser;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.protocol.ProtocolAddress;
import com.mes.connect.protocol.ProtocolType;

/**
 * S7地址解析器
 *
 * @author yzx
 * @version 1.0
 */
public class S7OldAddressParser implements AddressParser {
    /**
     * modbusTcp协议
     */
    private final String protocolS7 = "S7";
    /**
     * 字符串截取
     */
    private final String protocolS7_ = "S7.";
    /**
     * 地址类型
     */
    private final String addressTypeDB = "DB";
    /**
     * 地址类型
     */
    private final String addressTypeX = "X";
    /**
     * 地址类型
     */
    private final String addressTypeI = "I";
    /**
     * 地址类型
     */
    private final String addressTypeQ = "Q";
    /**
     * 地址类型
     */
    private final String addressTypeM = "M";

    /**
     * 地址值类型
     */
    private final String valueTypeDBW = "DBW";

    /**
     * 地址值类型
     */
    private final String valueTypeDBX = "DBX";

    /**
     * 地址值类型
     */
    private final String valueTypeDBD = "DBD";

    /**
     * 离散输入
     */
    private final int functionCode0x84 = 0x84;
    /**
     * 读写功能码
     */
    private final int functionCode4 = 4;

    @Override
    public ProtocolAddress parse(String address) {
        // 格式示例: "S7.DB1.DBX10.2" 或 "S7.M100"
        //位地址：S7.DB1.DBX0.0 (DB1 块中的字节 0 的位 0)
        //字节地址：S7.DB1.DBB0 (DB1 块中的字节 0)
        //字地址：S7.DB1.DBW0 (DB1 块中的字 0)
        //双字地址：S7.DB1.DBD0 (DB1 块中的双字 0)
        if (!address.startsWith(protocolS7_)) {
            throw new IllegalArgumentException("Invalid S7 address format: " + address);
        }

        String[] parts = address.substring(3).split("\\.");
        // parts =地址类型  字节长度>2
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid S7 address format: " + address);
        }

        // 解析DB号
        int dbNumber = 0;
        // 默认DB区域
        int area = 0x84;
        if (parts[0].startsWith(addressTypeDB)) {
            dbNumber = Integer.parseInt(parts[0].substring(2));
        } else if (addressTypeI.equals(parts[0])) {
            // 输入区域
            area = 0x81;
        } else if (addressTypeQ.equals(parts[0])) {
            // 输出区域
            area = 0x82;
        } else if (addressTypeM.equals(parts[0])) {
            // 内存区域
            area = 0x83;
        } else {
            throw new IllegalArgumentException("Invalid S7 area: " + parts[0]);
        }

        // 解析地址类型和地址值
        String addressPart = parts[1];
        int addressValue = 0;
        int bit = 0;

        if (area == functionCode0x84) {
            // DB区域
            if (addressPart.startsWith(valueTypeDBX)) {
                // 位地址
                String[] bitParts = addressPart.substring(3).split("\\.");
                addressValue = Integer.parseInt(bitParts[0]);
                if (bitParts.length > 1) {
                    bit = Integer.parseInt(bitParts[1]);
                }
            } else if (addressPart.startsWith(valueTypeDBW)) {
                // 字地址
                addressValue = Integer.parseInt(addressPart.substring(3));
            } else if (addressPart.startsWith(valueTypeDBD)) {
                // 双字地址
                addressValue = Integer.parseInt(addressPart.substring(3));
            } else {
                throw new IllegalArgumentException("Invalid S7 DB address type: " + addressPart);
            }
        } else {
            // I/Q/M区域
            if (addressPart.startsWith(addressTypeX)) {
                // 位地址
                String[] bitParts = addressPart.substring(1).split("\\.");
                addressValue = Integer.parseInt(bitParts[0]);
                if (bitParts.length > 1) {
                    bit = Integer.parseInt(bitParts[1]);
                }
            } else {
                // 字地址
                addressValue = Integer.parseInt(addressPart);
            }
        }

        return new ProtocolAddress(ProtocolType.S7, area, dbNumber, addressValue, bit);
    }

}    