package com.mes.connect;

import com.mes.connect.industrialinterface.IndustrialDataHandler;
import com.mes.connect.addressparser.ModbusAddressParser;
import com.mes.connect.protocol.ProtocolAddress;

/**
 * ExampleDataHandler
 *
 * @author yzx
 * @version 1.0
 */
public class ExampleDataHandler implements IndustrialDataHandler {
    /**
     * 线圈
     */
    private final boolean[] coils = new boolean[1000];
    /**
     * 离散型
     */
    private final boolean[] discreteInputs = new boolean[1000];
    /**
     * 存储寄存器内容
     */
    private final int[] holdingRegisters = new int[1000];
    /**
     * 输入
     */
    private final int[] inputRegisters = new int[1000];

    /**
     * S7协议
     */
    private final String protocolS7="S7";
    /**
     * S7协议
     */
    private final String protocolS7_="S7.";
    /**
     * S7协议
     */
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
    private final int functionCode1=1;
    /**
     * 离散输入
     */
    private final int functionCode2=2;
    /**
     * 离散输入
     */
    private final int functionCode3=3;
    /**
     * 离散输入
     */
    private final int functionCode4=4;
    /**
     * 离散输入
     */
    private final int functionCode5=5;
    /**
     * 读写功能码
     */
    private final int functionCode16=16;

    @Override
    public boolean handleReadBit(String address) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == functionCode1) {
            int index = parsedAddress.getAddress();
            return coils[index];
        } else if (parsedAddress.getFunctionCode() == functionCode2) {
            int index = parsedAddress.getAddress();
            return discreteInputs[index];
        }
        return false;
    }

    @Override
    public void handleWriteBit(String address, boolean value) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == 1) {
            // 线圈
            int index = parsedAddress.getAddress();
            coils[index] = value;
            System.out.println("写入线圈 " + address + " 值: " + value);
        }
    }

    @Override
    public int handleReadRegister(String address) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == functionCode3) {
            // 保持寄存器
            int index = parsedAddress.getAddress();
            return holdingRegisters[index];
        } else if (parsedAddress.getFunctionCode() == functionCode4) {
            // 输入寄存器
            int index = parsedAddress.getAddress();
            return inputRegisters[index];
        }
        return 0;
    }

    @Override
    public void handleWriteRegister(String address, int value) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == functionCode3) {
            // 保持寄存器
            int index = parsedAddress.getAddress();
            holdingRegisters[index] = value;
            System.out.println("写入寄存器 " + address + " 值: " + value);
        }
    }

    @Override
    public void handleWriteRegisters(String address, int[] values) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == functionCode16) {
            // 写多个寄存器
            int index = parsedAddress.getAddress();
            System.arraycopy(values, 0, holdingRegisters, index, values.length);
            System.out.println("写入寄存器 " + address + " 数量: " + values.length);
        }
    }

    @Override
    public int[] handleReadRegisters(String address, int quantity) {
        ProtocolAddress parsedAddress = new ModbusAddressParser().parse(address);
        if (parsedAddress.getFunctionCode() == functionCode3) {
            // 保持寄存器
            int startIndex = parsedAddress.getAddress();
            int[] result = new int[quantity];
            System.arraycopy(holdingRegisters, startIndex, result, 0, Math.min(quantity, holdingRegisters.length - startIndex));
            return result;
        } else if (parsedAddress.getFunctionCode() == functionCode4) {
            // 输入寄存器
            int startIndex = parsedAddress.getAddress();
            int[] result = new int[quantity];
            System.arraycopy(inputRegisters, startIndex, result, 0, Math.min(quantity, inputRegisters.length - startIndex));
            return result;
        } else if (address.startsWith(protocolS7_)) {
            return handleS7ReadRegisters(address, quantity);
        }
        return new int[0];
    }

    private int[] handleS7ReadRegisters(String address, int quantity) {
        // 解析S7地址格式并读取多个寄存器
        String[] parts = address.split("\\.");
        if (parts.length >= functionCode3 && protocolS7.equals(parts[0]) && parts[1].startsWith(addressTypeDB)) {
            try {
                int dbNumber = Integer.parseInt(parts[1].substring(2));
                String type = parts[2].substring(0, 3);
                int startIndex = Integer.parseInt(parts[2].substring(3));

                int baseIndex = dbNumber * 1000 + startIndex;
                int[] result = new int[quantity];

                if (valueTypeDBW.equals(type)) {
                    // 字(16位)数组
                    for (int i = 0; i < quantity && baseIndex + i < holdingRegisters.length; i++) {
                        result[i] = holdingRegisters[baseIndex + i];
                    }
                } else if (valueTypeDBD.equals(type)) {
                    // 双字(32位)数组，每个双字占两个寄存器位置
                    for (int i = 0; i < quantity && baseIndex + i * 2 + 1 < holdingRegisters.length; i++) {
                        result[i] = (holdingRegisters[baseIndex + i * 2] << 16) |
                                (holdingRegisters[baseIndex + i * 2 + 1] & 0xFFFF);
                    }
                }

                return result;
            } catch (NumberFormatException e) {
                System.err.println("地址解析错误: " + address);
            }
        }
        return new int[0];
    }

    @Override
    public float handleReadFloat(String address) {
        int[] registers = handleReadRegisters(address, 2);
        if (registers.length >= functionCode2) {
            int intBits = (registers[0] << 16) | registers[1];
            return Float.intBitsToFloat(intBits);
        }
        return 0.0f;
    }

    @Override
    public void handleWriteFloat(String address, float value) {
        int intBits = Float.floatToIntBits(value);
        int highWord = (intBits >> 16) & 0xFFFF;
        int lowWord = intBits & 0xFFFF;
        handleWriteRegisters(address, new int[]{highWord, lowWord});
    }

    @Override
    public String handleReadString(String address, int length) {
        int[] registers = handleReadRegisters(address, (length + 1) / 2);
        byte[] bytes = new byte[registers.length * 2];

        for (int i = 0; i < registers.length; i++) {
            bytes[i * 2] = (byte) ((registers[i] >> 8) & 0xFF);
            bytes[i * 2 + 1] = (byte) (registers[i] & 0xFF);
        }

        return new String(bytes, 0, length);
    }

    @Override
    public void handleWriteString(String address, String value) {
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

        handleWriteRegisters(address, registers);
    }
}