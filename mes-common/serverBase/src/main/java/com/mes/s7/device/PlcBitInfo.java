package com.mes.s7.device;
/**
 * PlcBitInfo class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class PlcBitInfo {

    /**
     * 构造函数，根据起始地址初始化对象。
     *
     * @param startAddress 起始地址
     */
    public PlcBitInfo(String startAddress) {
        this.startAddress = startAddress;
    }

    /** 起始地址 */
    private String startAddress;

    /** 参数标识 */
    private String codeId;

    /** 参数名称 */
    private String name;

    /** 读取参数值 */
    private Boolean value;

    /** 参数地址索引 */
    private int addressIndex;

    /**
     * 获取参数标识。
     *
     * @return 参数标识
     */
    public String getCodeId() {
        return this.codeId;
    }

    /**
     * 设置参数标识。
     *
     * @param codeId 参数标识
     */
    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    /**
     * 获取参数名称。
     *
     * @return 参数名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置参数名称。
     *
     * @param name 参数名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取参数值。
     *
     * @return 参数值
     */
    public Boolean getValue() {
        return this.value;
    }

    /**
     * 设置参数值。
     *
     * @param value 参数值
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    /**
     * 获取参数地址索引。
     *
     * @return 地址索引
     */
    public int getAddressIndex() {
        return this.addressIndex;
    }

    /**
     * 获取地址
     *
     * @param index 索引地址
     */
    public String getAddress(int index) {
        String[] stringdatas = this.startAddress.trim().split("\\.");
        if (stringdatas.length < 2) {
            return null;
        }
        int dbwindex = 0;
        int bitindex = 0;
        if (stringdatas.length == 3) {
            dbwindex = Integer.parseInt(stringdatas[1]);
            bitindex = Integer.parseInt(stringdatas[2]);
        } else {
            return null;
        }
        dbwindex += index / 8;
        bitindex += index % 8;
        return stringdatas[0] + "." + dbwindex + "." + bitindex;
    }

    /**
     * 获取地址
     */
    public String getAddress() {
        return getAddress(this.addressIndex);
    }

    /**
     * 设置地址
     */
    public void setAddressIndex(int addressindex) {
        this.addressIndex = addressindex;
    }
}
