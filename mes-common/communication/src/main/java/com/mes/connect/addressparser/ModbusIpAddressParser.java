package com.mes.connect.addressparser;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.protocol.ProtocolAddress;

/**
 * Modbus IP地址解析器
 *
 * @author yzx
 * @version 1.0
 */
public class ModbusIpAddressParser implements AddressParser {
    /**
     * 地址值类型
     */
    private final String valueTypeMBIP="MBIP.";
    @Override
    public ProtocolAddress parse(String address) {
        // 格式示例: "MBIP.4x0001" 或 "MBIP.0x100.2"
        if (!address.startsWith(valueTypeMBIP)) {
            throw new IllegalArgumentException("Invalid Modbus IP address format: " + address);
        }
        // 复用Modbus地址解析逻辑
        return new ModbusAddressParser().parse("MB." + address.substring(5));
    }
}    