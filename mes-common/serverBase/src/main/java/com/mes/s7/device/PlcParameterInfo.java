package com.mes.s7.device;

/**
 * PlcParameterInfo class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class PlcParameterInfo {
    /**
     * 构造方法，指定起始地址。
     *
     * @param startAddress 起始地址
     */
    public PlcParameterInfo(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * 起始地址（如 DB 块起始位置）
     */
    private String startAddress;

    /**
     * 参数标识（如 codeId，用于唯一标识该参数）
     */
    private String codeId;

    /**
     * 参数名称（中文或业务描述）
     */
    private String name;

    /**
     * 参数值（读取值，字符串表示）
     */
    private String value;

    /** 写入参数值（暂未使用） */
    // private String writeValue;

    /**
     * 参数单位（如 mm、℃ 等）
     */
    private String unit;

    /**
     * 参数值转换系数（用于缩放、精度处理）
     */
    private int ratio;

    /**
     * 参数地址索引（在数据块中的偏移位置）
     */
    private int addressIndex;

    /**
     * 参数地址长度（占用多少位/字节）
     */
    private int addressLength;

    /**
     * 获取参数标识。
     *
     * @return 参数标识 codeId
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
     * @return 参数值（字符串格式）
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 设置参数值。
     *
     * @param value 参数值（字符串格式）
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取参数单位。
     *
     * @return 参数单位
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * 设置参数单位。
     *
     * @param unit 参数单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
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
     * 设置参数地址索引。
     *
     * @param addressIndex 地址索引
     */
    public void setAddressIndex(int addressIndex) {
        this.addressIndex = addressIndex;
    }

    /**
     * 获取参数地址长度。
     *
     * @return 地址长度
     */
    public int getAddressLength() {
        return this.addressLength;
    }

    /**
     * 设置参数地址长度。
     *
     * @param addressLength 地址长度
     */
    public void setAddressLength(int addressLength) {
        this.addressLength = addressLength;
    }

    /**
     * 获取参数转换系数。
     *
     * @return 转换系数
     */
    public int getRatio() {
        return this.ratio;
    }

    /**
     * 设置参数转换系数。
     *
     * @param ratio 转换系数
     */
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    /**
     * 获取地址
     *
     * @param index 索引地址
     */
    public String getAddress(int index) {
        String[] stringdatas = this.startAddress.trim().split("\\.");
        int addressLength = this.addressLength;
        if (addressLength < 2) {
            return null;
        }
        int wordindex = Integer.parseInt(stringdatas[1]) + index;
        return stringdatas[0] + "." + wordindex;
    }

    /**
     * 获取地址
     */
    public String getAddress() {
        return getAddress(this.addressIndex);
    }
}