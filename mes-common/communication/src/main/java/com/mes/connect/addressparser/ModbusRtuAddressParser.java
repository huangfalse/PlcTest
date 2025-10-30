package com.mes.connect.addressparser;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.protocol.ProtocolAddress;

/**
 * Modbus RTU地址解析器
 */
class ModbusRtuAddressParser implements AddressParser {
    @Override
    public ProtocolAddress parse(String address) {
        // 复用Modbus地址解析逻辑
        return new ModbusAddressParser().parse(address);
    }
}    