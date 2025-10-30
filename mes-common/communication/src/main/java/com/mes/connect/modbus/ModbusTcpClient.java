package com.mes.connect.modbus;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.industrialinterface.IndustrialClient;
import com.mes.connect.addressparser.ModbusAddressParser;
import com.mes.connect.protocol.ProtocolAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Modbus TCP客户端实现
 *
 * @author yzx
 * @version 1.0
 */
public class ModbusTcpClient implements IndustrialClient {
    private static final Logger logger = Logger.getLogger(ModbusTcpClient.class.getName());
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final String host;
    private final int port;
    private final int unitId;
    private int transactionId = 0;
    private boolean connected;
    private final AddressParser addressParser = new ModbusAddressParser();
    /**
     * 线圈
     */
    private final int functionCode0x80 = 0x80;
    /**
     * 线圈
     */
    private final int functionCode0x02 = 0x02;
    /**
     * 线圈
     */
    private final int functionCode0xFF = 0xFF;
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
     * 离散输入
     */
    private final int functionCode8 = 8;
    /**
     * 读写功能码
     */
    private final int functionCode16 = 16;

    public ModbusTcpClient(String host, int port, int unitId) {
        this.host = host;
        this.port = port;
        this.unitId = unitId;
    }

    @Override
    public synchronized void connect() throws IOException {
        if (!connected) {
            socket = new Socket(host, port);
            // 5秒超时
            socket.setSoTimeout(5000);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            connected = true;
            logger.info("Connected to Modbus TCP server: " + host + ":" + port);
        }
    }

    @Override
    public synchronized void disconnect() {
        if (connected) {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.warning("Error closing Modbus TCP connection: " + e.getMessage());
            } finally {
                outputStream = null;
                inputStream = null;
                socket = null;
                connected = false;
                logger.info("Disconnected from Modbus TCP server");
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connected && socket != null && socket.isConnected() && !socket.isClosed();
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
        if (functionCode != functionCode3 && functionCode != functionCode4) {
            throw new IllegalArgumentException("Invalid function code for reading register: " + functionCode);
        }

        byte[] request = buildReadRequest(functionCode, parsedAddress.getAddress(), 1);
        byte[] response = sendRequest(request);
        // modbus 协议 第8个字节=功能码
        if (response[8] == functionCode0x02) {
            // 2字节数据
            return ((response[9] & 0xFF) << 8) | (response[10] & 0xFF);
        }

        throw new IOException("Unexpected response format for reading register");
    }

    @Override
    public void writeRegister(String address, int value) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        // 写单个寄存器
        int functionCode = 6;

        byte[] request = new byte[12];
        // 事务标识符高字节
        request[0] = (byte) (transactionId >> 8);
        // 事务标识符低字节
        request[1] = (byte) transactionId++;
        // 协议标识符高字节 (0)
        request[2] = 0;
        // 协议标识符低字节 (0)
        request[3] = 0;
        // 长度高字节
        request[4] = 0;
        // 长度低字节 (6)
        request[5] = 6;
        // 单元标识符
        request[6] = (byte) unitId;
        // 功能码
        request[7] = (byte) functionCode;
        // 地址高字节
        request[8] = (byte) (parsedAddress.getAddress() >> 8);
        // 地址低字节
        request[9] = (byte) parsedAddress.getAddress();
        // 值高字节
        request[10] = (byte) (value >> 8);
        // 值低字节
        request[11] = (byte) value;

        byte[] response = sendRequest(request);

        // 验证响应
        // modbus 协议 第七个字节=功能码
        if (response[7] != functionCode) {
            throw new IOException("Modbus exception: " + (response[7] & 0xFF));
        }
    }

    @Override
    public int[] readRegisters(String address, int quantity) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        int functionCode = parsedAddress.getFunctionCode();
        if (functionCode != functionCode3 && functionCode != functionCode4) {
            throw new IllegalArgumentException("Invalid function code for reading registers: " + functionCode);
        }

        byte[] request = buildReadRequest(functionCode, parsedAddress.getAddress(), quantity);
        byte[] response = sendRequest(request);

        int byteCount = response[8] & 0xFF;
        int[] result = new int[byteCount / 2];

        for (int i = 0; i < result.length; i++) {
            int index = 9 + i * 2;
            result[i] = ((response[index] & 0xFF) << 8) | (response[index + 1] & 0xFF);
        }

        return result;
    }

    @Override
    public void writeRegisters(String address, int[] values) throws IOException {
        ProtocolAddress parsedAddress = addressParser.parse(address);
        // 写多个寄存器
        int functionCode = 16;

        int byteCount = values.length * 2;
        int requestLength = 9 + byteCount;
        byte[] request = new byte[requestLength];
        // 事务标识符高字节
        request[0] = (byte) (transactionId >> 8);
        // 事务标识符低字节
        request[1] = (byte) transactionId++;
        // 协议标识符高字节 (0)
        request[2] = 0;
        // 协议标识符低字节 (0)
        request[3] = 0;
        // 长度高字节
        request[4] = (byte) ((requestLength - 6) >> 8);
        // 长度低字节
        request[5] = (byte) (requestLength - 6);
        // 单元标识符
        request[6] = (byte) unitId;
        // 功能码
        request[7] = (byte) functionCode;
        // 地址高字节
        request[8] = (byte) (parsedAddress.getAddress() >> 8);
        // 地址低字节
        request[9] = (byte) parsedAddress.getAddress();
        // 寄存器数量高字节
        request[10] = (byte) (values.length >> 8);
        // 寄存器数量低字节
        request[11] = (byte) values.length;
        // 字节数
        request[12] = (byte) byteCount;

        // 填充寄存器值
        for (int i = 0; i < values.length; i++) {
            int index = 13 + i * 2;
            // 值高字节
            request[index] = (byte) (values[i] >> 8);
            // 值低字节
            request[index + 1] = (byte) values[i];
        }

        byte[] response = sendRequest(request);

        // 验证响应
        // modbus 协议 第七个字节=功能码
        if (response[7] != functionCode) {
            throw new IOException("Modbus exception: " + (response[7] & 0xFF));
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

    private byte[] buildReadRequest(int functionCode, int address, int quantity) {
        byte[] request = new byte[12];
        // 事务标识符高字节
        request[0] = (byte) (transactionId >> 8);
        // 事务标识符低字节
        request[1] = (byte) transactionId++;
        // 协议标识符高字节 (0)
        request[2] = 0;
        // 协议标识符低字节 (0)
        request[3] = 0;
        // 长度高字节
        request[4] = 0;
        // 长度低字节 (6)
        request[5] = 6;
        // 单元标识符
        request[6] = (byte) unitId;
        // 功能码
        request[7] = (byte) functionCode;
        // 地址高字节
        request[8] = (byte) (address >> 8);
        // 地址低字节
        request[9] = (byte) address;
        // 数量高字节
        request[10] = (byte) (quantity >> 8);
        // 数量低字节
        request[11] = (byte) quantity;

        return request;
    }

    private byte[] sendRequest(byte[] request) throws IOException {
        if (!isConnected()) {
            connect();
        }

        // 发送请求
        outputStream.write(request);
        outputStream.flush();

        // 接收响应
        byte[] header = new byte[6];
        int bytesRead = 0;

        // 读取头部 前头部长度6字节
        while (bytesRead < 6) {
            int count = inputStream.read(header, bytesRead, 6 - bytesRead);
            if (count == -1) {
                throw new IOException("Connection closed while reading response header");
            }
            bytesRead += count;
        }

        int length = ((header[4] & 0xFF) << 8) | (header[5] & 0xFF);
        byte[] response = new byte[6 + length];

        // 复制头部
        System.arraycopy(header, 0, response, 0, 6);

        // 读取剩余部分
        bytesRead = 0;
        while (bytesRead < length) {
            int count = inputStream.read(response, 6 + bytesRead, length - bytesRead);
            if (count == -1) {
                throw new IOException("Connection closed while reading response body");
            }
            bytesRead += count;
        }
        // 检查事务标识符
        if ((response[0] & functionCode0xFF) != (request[0] & functionCode0xFF) || (response[1] & functionCode0xFF) != (request[1] & functionCode0xFF)) {
            logger.warning("Transaction ID mismatch in response - expected: " +
                    ((request[0] & 0xFF) << 8 | (request[1] & 0xFF)) +
                    ", got: " + ((response[0] & 0xFF) << 8 | (response[1] & 0xFF)));
            // 虽然事务ID不匹配，但可能是之前请求的响应，选择继续处理而不是抛出异常
        }

        // 检查功能码（如果有异常，功能码会是0x80 + 原功能码）
        if ((response[7] & 0xFF) > functionCode0x80) {
            int exceptionCode = response[8] & 0xFF;
            throw new IOException("Modbus exception: " + exceptionCode);
        }

        return response;
    }
}    