package com.mes.connect.modbus;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.industrialinterface.IndustrialClient;
import com.mes.connect.addressparser.ModbusIpAddressParser;
import com.mes.connect.protocol.ProtocolAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Modbus IP客户端实现（基于UDP）
 *
 * @author yzx
 * @version 1.0
 */
public class ModbusIpClient implements IndustrialClient {
    private static final Logger logger = Logger.getLogger(ModbusIpClient.class.getName());
    private DatagramSocket socket;
    private final String host;
    private final int port;
    private boolean connected;
    private int transactionId = 0;
    private final AddressParser addressParser = new ModbusIpAddressParser();
    /**
     * 线圈
     */
    private final int functionCode0x80 = 0x80;
    /**
     * 离散输入
     */
    private final int functionCode2 = 2;
    /**
     * 离散输入
     */
    private final int functionCode3 = 3;
    /**
     * 离散输入
     */
    private final int functionCode4 = 4;
    /**
     * 离散输入
     */
    private final int functionCode6 = 6;
    /**
     * 离散输入
     */
    private final int functionCode7 = 7;
    /**
     * 读写功能码
     */
    private final int functionCode16 = 16;

    public ModbusIpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public synchronized void connect() throws IOException {
        if (!connected) {
            socket = new DatagramSocket();
            // 5秒超时
            socket.setSoTimeout(5000);
            connected = true;
            logger.info("Connected to Modbus IP server: " + host + ":" + port);
        }
    }

    @Override
    public synchronized void disconnect() {
        if (connected) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } finally {
                socket = null;
                connected = false;
                logger.info("Disconnected from Modbus IP server");
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }


    @Override
    public boolean readBit(String address) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        String[] parts = address.split("\\.");
        String strAddress = "";
        if (parts.length > 1) {
            strAddress = parts[0] + "." + parts[1];
        }
        int[] data = readRegisters(strAddress, 1);
        int byteValue = data[0];
        return (byteValue & (1 << parsedAddress.getBit())) != 0;
    }

    @Override
    public void writeBit(String address, boolean value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        String[] parts = address.split("\\.");
        String strAddress = "";
        if (parts.length > 1) {
            strAddress = parts[0] + "." + parts[1];
        }
        int[] currentData = readRegisters(strAddress, 1);
        int byteValue = currentData[0];
        if (value) {
            byteValue |= (1 << parsedAddress.getBit());
        } else {
            byteValue &= ~(1 << parsedAddress.getBit());
        }
        writeRegister(strAddress, byteValue);
    }

    @Override
    public int readRegister(String address) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int functionCode = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();

        if (functionCode != functionCode3 && functionCode != functionCode4) {
            throw new IllegalArgumentException("Invalid function code for register operation: " + functionCode);
        }

        byte[] request = buildReadRequest(functionCode, startAddress, 1);
        byte[] response = sendRequest(request);
        // modbus 协议 第七个字节=功能码
        if (response[7] == functionCode) {
            int byteCount = response[8] & 0xFF;
            //字节长度>=2
            if (byteCount >= 2) {
                return ((response[9] & 0xFF) << 8) | (response[10] & 0xFF);
            }
        } else if (response[7] == functionCode + functionCode0x80) {
            // modbus 协议 第七个字节=功能码
            int exceptionCode = response[8] & 0xFF;
            throw new IOException("Modbus exception: " + exceptionCode);
        }

        throw new IOException("Invalid Modbus response");
    }

    @Override
    public void writeRegister(String address, int value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int functionCode = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();

        if (functionCode != functionCode6) {
            throw new IllegalArgumentException("Invalid function code for write register operation: " + functionCode);
        }

        byte[] request = buildWriteSingleRegisterRequest(startAddress, value);
        byte[] response = sendRequest(request);
        // modbus 协议 第七个字节=功能码
        if (response[7] != functionCode) {
            if (response[7] == functionCode + functionCode0x80) {
                int exceptionCode = response[8] & 0xFF;
                throw new IOException("Modbus exception: " + exceptionCode);
            }
            throw new IOException("Invalid Modbus response");
        }
    }

    @Override
    public int[] readRegisters(String address, int quantity) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int functionCode = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();

        if (functionCode != functionCode3 && functionCode != functionCode4) {
            throw new IllegalArgumentException("Invalid function code for register operation: " + functionCode);
        }

        byte[] request = buildReadRequest(functionCode, startAddress, quantity);
        byte[] response = sendRequest(request);
        // modbus 协议 第七个字节=功能码
        if (response[7] == functionCode) {
            int byteCount = response[8] & 0xFF;
            //一个字 由两个字节组成
            if (byteCount == quantity * 2) {
                int[] result = new int[quantity];
                for (int i = 0; i < quantity; i++) {
                    result[i] = ((response[9 + i * 2] & 0xFF) << 8) | (response[10 + i * 2] & 0xFF);
                }
                return result;
            }
        } else if (response[7] == functionCode + functionCode0x80) {
            // modbus 协议 第七个字节=功能码
            int exceptionCode = response[8] & 0xFF;
            throw new IOException("Modbus exception: " + exceptionCode);
        }

        throw new IOException("Invalid Modbus response");
    }

    @Override
    public void writeRegisters(String address, int[] values) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int functionCode = parsedAddress.getFunctionCode();
        int startAddress = parsedAddress.getAddress();

        if (functionCode != functionCode16) {
            throw new IllegalArgumentException("Invalid function code for write registers operation: " + functionCode);
        }

        byte[] request = buildWriteMultipleRegistersRequest(startAddress, values);
        byte[] response = sendRequest(request);
        // modbus 协议 第七个字节=功能码
        if (response[7] != functionCode) {
            if (response[7] == functionCode + functionCode0x80) {
                int exceptionCode = response[8] & 0xFF;
                throw new IOException("Modbus exception: " + exceptionCode);
            }
            throw new IOException("Invalid Modbus response");
        }
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
        int[] registers = readRegisters(address, (length + 1) / 2);
        byte[] bytes = new byte[registers.length * 2];

        for (int i = 0; i < registers.length; i++) {
            bytes[i * 2] = (byte) ((registers[i] >> 8) & 0xFF);
            bytes[i * 2 + 1] = (byte) (registers[i] & 0xFF);
        }

        return new String(bytes, 0, length);
    }

    @Override
    public void writeString(String address, String value) throws IOException {
        byte[] bytes = value.getBytes();
        int[] registers = new int[(bytes.length + 1) / 2];

        for (int i = 0; i < bytes.length; i++) {
            int regIndex = i / 2;
            int byteIndex = i % 2;

            if (byteIndex == 0) {
                registers[regIndex] = (bytes[i] & 0xFF) << 8;
            } else {
                registers[regIndex] |= (bytes[i] & 0xFF);
            }
        }

        writeRegisters(address, registers);
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    private byte[] buildReadRequest(int functionCode, int startAddress, int quantity) {
        byte[] request = new byte[12];

        // 事务标识符
        request[0] = (byte) (transactionId >> 8);
        request[1] = (byte) transactionId;

        // 协议标识符 (0)
        request[2] = 0;
        request[3] = 0;

        // 长度 (6)
        request[4] = 0;
        request[5] = 6;

        // 单元标识符 (1)
        request[6] = 1;

        // 功能码
        request[7] = (byte) functionCode;

        // 起始地址
        request[8] = (byte) (startAddress >> 8);
        request[9] = (byte) startAddress;

        // 数量
        request[10] = (byte) (quantity >> 8);
        request[11] = (byte) quantity;

        transactionId = (transactionId + 1) % 65536;

        return request;
    }

    private byte[] buildWriteSingleCoilRequest(int address, int value) {
        byte[] request = new byte[12];

        // 事务标识符
        request[0] = (byte) (transactionId >> 8);
        request[1] = (byte) transactionId;

        // 协议标识符 (0)
        request[2] = 0;
        request[3] = 0;

        // 长度 (6)
        request[4] = 0;
        request[5] = 6;

        // 单元标识符 (1)
        request[6] = 1;

        // 功能码 (5)
        request[7] = 5;

        // 地址
        request[8] = (byte) (address >> 8);
        request[9] = (byte) address;

        // 值
        request[10] = (byte) (value >> 8);
        request[11] = (byte) value;

        transactionId = (transactionId + 1) % 65536;

        return request;
    }

    private byte[] buildWriteSingleRegisterRequest(int address, int value) {
        byte[] request = new byte[12];

        // 事务标识符
        request[0] = (byte) (transactionId >> 8);
        request[1] = (byte) transactionId;

        // 协议标识符 (0)
        request[2] = 0;
        request[3] = 0;

        // 长度 (6)
        request[4] = 0;
        request[5] = 6;

        // 单元标识符 (1)
        request[6] = 1;

        // 功能码 (6)
        request[7] = 6;

        // 地址
        request[8] = (byte) (address >> 8);
        request[9] = (byte) address;

        // 值
        request[10] = (byte) (value >> 8);
        request[11] = (byte) value;

        transactionId = (transactionId + 1) % 65536;

        return request;
    }

    private byte[] buildWriteMultipleRegistersRequest(int address, int[] values) {
        int byteCount = values.length * 2;
        byte[] request = new byte[13 + byteCount];

        // 事务标识符
        request[0] = (byte) (transactionId >> 8);
        request[1] = (byte) transactionId;

        // 协议标识符 (0)
        request[2] = 0;
        request[3] = 0;

        // 长度 (7 + byteCount)
        request[4] = 0;
        request[5] = (byte) (7 + byteCount);

        // 单元标识符 (1)
        request[6] = 1;

        // 功能码 (16)
        request[7] = 16;

        // 地址
        request[8] = (byte) (address >> 8);
        request[9] = (byte) address;

        // 寄存器数量
        request[10] = (byte) (values.length >> 8);
        request[11] = (byte) values.length;

        // 字节数
        request[12] = (byte) byteCount;

        // 值
        for (int i = 0; i < values.length; i++) {
            request[13 + i * 2] = (byte) (values[i] >> 8);
            request[14 + i * 2] = (byte) values[i];
        }

        transactionId = (transactionId + 1) % 65536;

        return request;
    }

    private byte[] sendRequest(byte[] request) throws IOException {
        if (!isConnected()) {
            connect();
        }

        InetAddress address = InetAddress.getByName(host);
        DatagramPacket sendPacket = new DatagramPacket(request, request.length, address, port);

        // 发送请求
        socket.send(sendPacket);

        // 接收响应
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);

        return Arrays.copyOf(receiveBuffer, receivePacket.getLength());
    }
}