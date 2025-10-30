package com.mes.connect.protocol;


/**
 * 协议地址类
 *
 * @author yzx
 * @version 1.0
 */
public class ProtocolAddress {
    private final ProtocolType protocol;
    private final int functionCode;
    private final int dbNumber;
    private final int address;
    private final int bit;

    /**
     * @param protocol
     * @param functionCode
     * @param dbNumber
     * @param address
     * @param bit
     */
    public ProtocolAddress(ProtocolType protocol, int functionCode, int dbNumber, int address, int bit) {
        this.protocol = protocol;
        this.functionCode = functionCode;
        this.dbNumber = dbNumber;
        this.address = address;
        this.bit = bit;
    }

    /**
     * @return
     */
    public ProtocolType getProtocol() {
        return protocol;
    }

    /**
     * @return
     */
    public int getFunctionCode() {
        return functionCode;
    }

    /**
     * @return
     */
    public int getDbNumber() {
        return dbNumber;
    }

    /**
     * @return
     */
    public int getAddress() {
        return address;
    }

    /**
     * @return
     */
    public int getBit() {
        return bit;
    }

    @Override
    public String toString() {
        return "ProtocolAddress{" +
                "protocol=" + protocol +
                ", functionCode=" + functionCode +
                ", dbNumber=" + dbNumber +
                ", address=" + address +
                ", bit=" + bit +
                '}';
    }
}    