package com.mes.connect.s7;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.industrialinterface.IndustrialClient;
import com.mes.connect.addressparser.S7OldAddressParser;
import com.mes.connect.protocol.ProtocolAddress;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * S7协议客户端实现
 *
 * @author yzx
 * @version 1.0
 */
@Slf4j
public class S7ClientOld implements IndustrialClient {
    private static final Logger logger = Logger.getLogger(S7ClientOld.class.getName());
    private final String host;
    private final int port;
    private final int rack;
    private final int slot;
    private boolean connected;
    private S7PLC s7Plc;
    private final AddressParser addressParser = new S7OldAddressParser();
    private EPlcType ePlcType;

    public S7ClientOld(String plcType, String host, int port, int rack, int slot) {
        this.host = host;
        this.port = port;
        this.rack = rack;
        this.slot = slot;
        switch (plcType) {
            case "S200_SMART":
                ePlcType = EPlcType.S200_SMART;
                break;
            case "S200":
                ePlcType = EPlcType.S200;
                break;
            case "S300":
                ePlcType = EPlcType.S300;
                break;
            case "S400":
                ePlcType = EPlcType.S400;
                break;
            case "S1200":
                ePlcType = EPlcType.S1200;
                break;
            case "S1500":
                ePlcType = EPlcType.S1500;
                break;
            default:
                throw new IllegalArgumentException("无效的西门子PLC类型: " + plcType);
        }
    }

    @Override
    public synchronized void connect() throws IOException {
        if (!connected) {
            this.s7Plc = new S7PLC(this.ePlcType, this.host, port, rack, slot);
            connected = true;
            logger.info("Connected to S7 server: " + host + ":" + port);
        }
    }

    @Override
    public synchronized void disconnect() {
        if (connected) {
            this.s7Plc.close();
        }
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public boolean readBit(String address) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int dbNumber = parsedAddress.getDbNumber();
        int area = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();
        int bit = parsedAddress.getBit();
        return this.s7Plc.readBoolean(getAddress(parsedAddress));
    }

    @Override
    public void writeBit(String address, boolean value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int dbNumber = parsedAddress.getDbNumber();
        int area = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();
        int bit = parsedAddress.getBit();
        this.s7Plc.writeBoolean(getAddress(parsedAddress), value);
    }

    @Override
    public int readRegister(String address) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        return this.s7Plc.readUInt16(getAddress(parsedAddress));
    }

    @Override
    public void writeRegister(String address, int value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        this.s7Plc.writeUInt16(getAddress(parsedAddress), value);
    }

    @Override
    public int[] readRegisters(String address, int quantity) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int dbNumber = parsedAddress.getDbNumber();
        int area = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();
        // 计算需要读取的字节数 (每个UInt16占用2字节)
        int byteCount = quantity * 2;
        // 读取字节数组
        byte[] bytes = this.s7Plc.readByte(getAddress(parsedAddress), byteCount);
        // 将字节转换为无符号整数数组
        int[] result = new int[quantity];
        for (int i = 0; i < quantity; i++) {
            int index = i * 2;
            // 组合两个字节为一个无符号16位整数
            result[i] = ((bytes[index] & 0xFF) << 8) | (bytes[index + 1] & 0xFF);
        }
        return result;
    }

    @Override
    public void writeRegisters(String address, int[] values) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int dbNumber = parsedAddress.getDbNumber();
        int area = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();
        // 转换整数数组为字节数组
        byte[] bytes = new byte[values.length * 2];
        for (int i = 0; i < values.length; i++) {
            int index = i * 2;
            // 将整数拆分为两个字节
            bytes[index] = (byte) ((values[i] >> 8) & 0xFF);
            bytes[index + 1] = (byte) (values[i] & 0xFF);
        }
        this.s7Plc.writeByte(getAddress(parsedAddress), bytes);
    }

    @Override
    public float readFloat(String address) throws IOException {
        int[] registers = readRegisters(address, 2);
        int intBits = (registers[0] << 16) | registers[1];
        return Float.intBitsToFloat(intBits);
    }

    @Override
    public void writeFloat(String address, float value) throws IOException {
        int intBits = Float.floatToIntBits(value);
        int highWord = (intBits >> 16) & 0xFFFF;
        int lowWord = intBits & 0xFFFF;
        writeRegisters(address, new int[]{highWord, lowWord});
    }

    @Override
    public String readString(String address, int length) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        return this.s7Plc.readString(getAddress(parsedAddress));
    }

    @Override
    public void writeString(String address, String value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        this.s7Plc.writeString(getAddress(parsedAddress), value);
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    private String getAddress(ProtocolAddress parsedAddress) {
        return "DB" + parsedAddress.getDbNumber() + "." + parsedAddress.getAddress();
    }
}    