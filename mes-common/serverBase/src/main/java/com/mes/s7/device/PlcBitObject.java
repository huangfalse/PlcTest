package com.mes.s7.device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PlcBitObject class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class PlcBitObject {

    /**
     * 该模块数据类型，数据起始位置
     */
    private String plcAddressBegin;

    /**
     * 数据地址长度：第一参数到最后一个参数的长度
     */
    private int plcAddressLength;

    /**
     * PLC 位信息映射表，key 为参数标识（codeId），value 为对应的 PLC 位信息对象。
     * 使用 LinkedHashMap 保证插入顺序不变。
     */
    private LinkedHashMap<String, PlcBitInfo> plcBitMap;

    /**
     * @return 数据区开始地址
     */
    public String getPlcAddressBegin() {
        return plcAddressBegin;
    }

    /**
     * @param plcAddressBegin 设置数据区开始地址
     */
    public void setPlcAddressBegin(String plcAddressBegin) {
        this.plcAddressBegin = plcAddressBegin;
    }

    /**
     * @return 数据区 读取所有数据所需的长度（以byte类型为基准）
     */
    public int getPlcAddressLength() {
        return plcAddressLength;
    }

    /**
     * @return 设置：数据区 读取所有数据所需的长度（以byte类型为基准）
     */
    public void setPlcAddressLength(int plcAddressLength) {
        this.plcAddressLength = plcAddressLength;
    }

    /**
     * @return 获取参数实例集合
     */
    public LinkedHashMap<String, PlcBitInfo> getBitMap() {
        return plcBitMap;
    }

    /**
     * 根据参数标识 获取某个参数实例
     *
     * @param codeid 参数标识
     * @return 获取某个参数实例
     */
    public PlcBitInfo getPlcBit(String codeid) {
        if (plcBitMap != null) {
            /*for (PlcBitInfo plcbitInfo : plcBitList) {
                if (plcbitInfo.getCodeId().equals(codeid))
                    return plcbitInfo;
            }*/
            return plcBitMap.get(codeid);
        } else {
            return null;
        }
    }

    /**
     * 根据参数标识 获取某个参数实例
     *
     * @param codeids 参数标识
     * @return 获取某个参数实例
     */
    public List<Boolean> getPlcBitValues(List<String> codeids) {
        List<Boolean> arrayList = new ArrayList<>();
        if (plcBitMap != null) {
            for (String codeId : codeids) {
                // 按照传入参数的顺序遍历
                arrayList.add(plcBitMap.get(codeId).getValue());
            }
        }
        return arrayList;
    }


    public List<String> getAddressListByCodeId(List<String> codeIdList) {
        List<String> addressList = new ArrayList<>();
        for (String codeId : codeIdList) {
            addressList.add(plcBitMap.get(codeId).getAddress());
        }
        return addressList;
    }


    /**
     * 添加参数实例
     *
     * @param param 参数实例
     */
    public void addPlcBit(PlcBitInfo param) {
        if (plcBitMap != null) {
            plcBitMap.put(param.getCodeId(), param);
        } else {
            plcBitMap = new LinkedHashMap<String, PlcBitInfo>();
            plcBitMap.put(param.getCodeId(), param);
        }
    }

    /**
     * 根据PLC返回的数据 给参数实例赋值
     *
     * @param plcValueArray PLC读取回来的byte类型数据集合
     */
    public void setPlcBitList(List<Boolean> plcValueArray) {
        if (plcBitMap != null) {
            Collection<PlcBitInfo> values = plcBitMap.values();
            for (PlcBitInfo plcbitInfo : values) {
                plcbitInfo.setValue(plcValueArray.get(plcbitInfo.getAddressIndex()));
            }
        }
    }


}