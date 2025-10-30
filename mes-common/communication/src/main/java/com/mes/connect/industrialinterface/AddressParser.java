package com.mes.connect.industrialinterface;

import com.mes.connect.protocol.ProtocolAddress;


/**
 * 地址解析器接口
 *
 * @author yzx
 * @version 1.0
 */
public interface AddressParser {
    /**
     * 解析地址
     * @param address
     * @return
     */
    ProtocolAddress parse(String address);
}    