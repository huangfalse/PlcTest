package com.mes.s7.device;

import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PlcParameterObject class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class PlcParameterObject {

    /**
     * 该模块数据类型，数据起始位置
     */
    private String plcAddressBegin;

    /**
     * 数据地址长度：第一参数到最后一个参数的长度
     */
    private int plcAddressLength;

    /**
     * PLC 参数信息映射表，key 为参数标识（如 codeId），value 为对应的 PLC 参数对象。
     * 使用 LinkedHashMap 可保持参数插入顺序不变，便于配置显示或遍历。
     */
    private LinkedHashMap<String, PlcParameterInfo> plcParameterMap;


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
    public LinkedHashMap<String, PlcParameterInfo> getPlcParameterMap() {
        return plcParameterMap;
    }

    /**
     * 根据参数标识 获取某个参数实例
     *
     * @param codeid 参数标识
     * @return 获取某个参数实例
     */
    public PlcParameterInfo getPlcParameter(String codeid) {
        if (plcParameterMap != null) {
            return plcParameterMap.get(codeid);
        } else {
            return null;
        }
        /*if (plcParameterList != null) {
            for (PlcParameterInfo plcParameterInfo : plcParameterList) {
                if (plcParameterInfo.getCodeId().equals(codeid))
                    return plcParameterInfo;
            }
            return null;
        } else
            return null;*/
    }


    /**
     * 根据参数标识 获取某个参数实例
     *
     * @param codeids 参数标识
     * @return 获取某个参数实例
     */
    public List<String> getPlcParameterValues(List<String> codeids) {
        List<String> arrayList = new ArrayList<>();
        if (plcParameterMap != null) {
            // 按照传入参数的顺序遍历
            for (String codeId : codeids) {
                PlcParameterInfo plcParameterInfo = plcParameterMap.get(codeId);
                if (plcParameterInfo != null) {
                    arrayList.add(plcParameterInfo.getValue());
                } else {
                    // 如果找不到对应的值，添加 null
                    arrayList.add(null);
                }
            }
        }
        return arrayList;
    }

    /**
     * 根据参数标识 获取传入参数的plc地址
     *
     * @param codeIdList 参数标识
     * @return 传入参数的plc地址
     */
    public List<String> getAddressListByCodeId(List<String> codeIdList) {
        List<String> addressList = new ArrayList<>();
        if (plcParameterMap != null) {
            for (String codeId : codeIdList) {
                String address = plcParameterMap.get(codeId).getAddress();
                if (address != null) {
                    addressList.add(address);
                }
            }
        }
        return addressList;
    }


    /**
     * 添加参数实例
     *
     * @param param 参数实例
     */
    public void addPlcParameter(PlcParameterInfo param) {
        if (plcParameterMap != null) {
            plcParameterMap.put(param.getCodeId(), param);
        } else {
            plcParameterMap = new LinkedHashMap<String, PlcParameterInfo>();
            plcParameterMap.put(param.getCodeId(), param);
        }
        /*if (plcParameterList != null)
            plcParameterList.add(param);
        else {
            plcParameterList = new ArrayList<PlcParameterInfo>();
            plcParameterList.add(param);
        }*/
    }

    /**
     * 根据PLC返回的数据 给参数实例赋值
     *
     * @param plcValueArray PLC读取回来的byte类型数据集合
     */
    public void setPlcParameterList(byte[] plcValueArray) {
        if (plcParameterMap != null) {
            Collection<PlcParameterInfo> values = plcParameterMap.values();
            for (PlcParameterInfo plcParameterInfo : values) {
                byte[] valueList = new byte[plcParameterInfo.getAddressLength()];
                for (int i = 0; i < plcParameterInfo.getAddressLength(); i++) {
                    Array.setByte(valueList, i, plcValueArray[plcParameterInfo.getAddressIndex() + i]);
                }
                if (plcParameterInfo.getAddressLength() == 2) {
                    plcParameterInfo.setValue(String.valueOf(ShortUtil.toUInt16(valueList)));
                } else if (plcParameterInfo.getAddressLength() == 4) {
                    plcParameterInfo.setValue(String.valueOf(IntegerUtil.toUInt32(valueList)));
                } else if (plcParameterInfo.getAddressLength() > 10) {
                    plcParameterInfo.setValue((byteToHexString(valueList)));
                } else {
                    plcParameterInfo.setValue((byteToHexString(valueList)));
                }
            }
        }
/*        if (plcParameterList != null) {

            for (PlcParameterInfo plcParameterInfo : plcParameterList) {

                byte[] valueList = new byte[plcParameterInfo.getAddressLength()];

//                System.out.println(plcParameterInfo.getAddressLength());

                for (int i = 0; i < plcParameterInfo.getAddressLength(); i++) {
                    Array.setByte(valueList, i, plcValueArray[plcParameterInfo.getAddressIndex() + i]);

                }

                if (plcParameterInfo.getAddressLength() == 2) {
                    plcParameterInfo.setValue(String.valueOf(byte2short(valueList)));
                } else if (plcParameterInfo.getAddressLength() == 4) {
                    plcParameterInfo.setValue(String.valueOf(byte2int(valueList)));
                }
                else if (plcParameterInfo.getAddressLength() >10) {
                    plcParameterInfo.setValue((byteToHexString(valueList)));
                } else {
                    String valuestr = new String(valueList);
                    plcParameterInfo.setValue(valuestr);
                }
            }
        }*/
    }

    /**
     * 把写入值转化为byte[]
     *
     * @param param 参数实例
     * @param data  写入值的字符类型
     */
    public byte[] setValueToBytes(PlcParameterInfo param, String data) {
        if (param.getAddressLength() == 2) {
            return ShortUtil.toByteArray(Integer.parseInt(data));

        } else if (param.getAddressLength() == 4) {

            return IntegerUtil.toByteArray(Long.parseLong(data));
        } else if (param.getAddressLength() > 10) {
            return data.getBytes();
        } else {
            return data.getBytes();
        }
    }

    /**
     * short类型转byte[]
     *
     * @param s short类型值
     */
    public static byte[] short2byte(short s) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            //计算偏移量
            int offset = 16 - (i + 1) * 8;
            //把16位分为2个8位进行分别存储
            b[i] = (byte) ((s >> offset) & 0xff);
        }
        return b;
    }

    /**
     * byte[]类型转short
     *
     * @param b byte[]类型值
     */
    public static short byte2short(byte[] b) {
        short l = 0;
        for (int i = 0; i < 2; i++) {
            //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l <<= 8;
            //和上面也是一样的  l = l | (b[i]&0xff)
            l |= (b[i] & 0xff);
        }
        return l;
    }

    /**
     * byte[]类型转short
     *
     * @param b byte[]类型值
     */
    public static int byte2int(byte[] b) {
        int l = 0;
        for (int i = 0; i < 4; i++) {
            //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l <<= 8;
            //和上面也是一样的  l = l | (b[i]&0xff)
            l |= (b[3 - i] & 0xff);
        }
        return l;
    }

    public static byte[] int2byte(int s) {
        byte[] b = new byte[2];
        for (int i = 0; i < 4; i++) {
            //因为byte占4个字节，所以要计算偏移量
            int offset = 16 - (i + 1) * 8;
            //把32位分为4个8位进行分别存储
            b[i] = (byte) ((s >> offset) & 0xff);
        }
        return b;
    }

    public static String byteToHexString(byte[] bytes) {
        String str = new String(bytes, StandardCharsets.UTF_8).trim();
        return str;
    }
}