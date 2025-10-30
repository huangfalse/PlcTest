package com.mes.s7;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.MultiAddressWrite;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;

import java.util.ArrayList;
import java.util.List;
/**
 * S7control class
 *
 * @author zsh
 * @date 2025/07/04
 */
public class S7control {

    S7PLC s7Plc;

    public S7control(EPlcType plcType, String ip, int port, int rack, int slot) {
        if (s7Plc == null) {
            s7Plc = new S7PLC(plcType, ip, port, 0, 0);
        }
    }

    /**
     * 关闭西门子s7通讯连接
     */
    public void closeS7client() {
        if (s7Plc != null) {
            s7Plc.close();
        }
    }

    /**
     * 重启西门子s7通讯连接
     */
    public boolean reStartS7client() {
        if (s7Plc != null) {
            try {
                s7Plc.hotRestart();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }


    /**
     * s7通讯连接状态
     */
    public boolean checkConnected() {
        return s7Plc.checkConnected();
    }

    /**
     * 按指定的地址 写入一个word
     *
     * @param address 地址
     * @param data    word的值
     */
    public boolean writeWord(String address, int data) {
        if (s7Plc == null) {
            return false;
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                s7Plc.writeUInt16(address, data);
                result = true;
            } catch (Exception ex) {
                System.out.println("向plc写命令过程中发生异常，原因为：" + ex.getMessage());
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;
    }

    /**
     * 从某地址连续 写入多个word
     *
     * @param address 地址
     * @param datas   word的值
     */
    public boolean writeWord(String address, List<Integer> datas) {
        if (s7Plc == null) {
            return false;
        }
        boolean result = false;
        int tryCount = 2;
        List<String> addresslist = getAddressList(address, datas.size(), 16);
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        for (int i = 0; i < datas.size(); i++) {
            addressWrite.addUInt16(addresslist.get(i), datas.get(i));
        }
        do {
            try {
                s7Plc.writeMultiData(addressWrite);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;

    }

    /**
     * 按指定的地址 写入多个word
     *
     * @param address 地址
     * @param datas   word的值
     */


    /**
     * 按指定的地址 写入一个Bit
     *
     * @param address 地址
     * @param data    Bit的值
     */
    public boolean writeBit(String address, Boolean data) {
        if (s7Plc == null) {
            return false;
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                s7Plc.writeBoolean(address, data);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;
    }

    /**
     * 按指定的地址 写入多个bit
     *
     * @param address 地址
     * @param datas   bit的值
     */
    public boolean writeBit(List<String> address, List<Boolean> datas) {
        if (s7Plc == null) {
            return false;
        }

        MultiAddressWrite addressWrite = new MultiAddressWrite();
        for (int i = 0; i < address.size(); i++) {
            addressWrite.addBoolean(address.get(i), datas.get(i));
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                s7Plc.writeMultiData(addressWrite);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;

    }

    /**
     * 从某地址连续 写入多个bit
     *
     * @param address 地址
     * @param datas   word的值
     */
    public boolean writeBit(String address, List<Boolean> datas) {
        if (s7Plc == null) {
            return false;
        }
        List<String> addresslist = getAddressList(address, datas.size(), 1);
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        for (int i = 0; i < datas.size(); i++) {
            addressWrite.addBoolean(addresslist.get(i), datas.get(i));
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                s7Plc.writeMultiData(addressWrite);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;
    }

    /**
     * 按指定的地址 写入多个byte
     *
     * @param address 地址
     * @param datas   byte的值
     */
    public boolean writeByte(String address, byte[] datas) {
        if (s7Plc == null) {
            return false;
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                s7Plc.writeByte(address, datas);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;
    }

    /**
     * 按指定的地址 读取word结果集
     *
     * @param address 地址
     * @return 结果
     */
    public List<Integer> readWord(List<String> address) {
        if (s7Plc == null) {
            return null;
        }
        List<Integer> result = null;
        try {
            result = s7Plc.readUInt16(address);
        } catch (Exception e) {
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());

        } finally {
            System.out.println("读取 " + address + " 最终：" + result);
        }
        return result;
    }


    /**
     * 按指定的地址 读取word结果集
     *
     * @param address 地址
     * @param count   连续读多少个word
     * @return 结果
     */
    public List<Integer> readWord(String address, int count) {
        if (s7Plc == null) {
            return null;
        }
        List<Integer> result = null;
        List<String> addresslist = getAddressList(address, count, 16);
        try {
            result = s7Plc.readUInt16(addresslist);
        } catch (Exception e) {
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());
        } finally {
            System.out.println("读取 " + address + " 最终：" + result);
        }
        return result;
    }

    /**
     * 按指定的地址 读取byte结果集
     *
     * @param address 地址
     * @param count   连续读多少个byte
     * @return 结果
     */
    public byte[] readByte(String address, int count) {
        if (s7Plc == null) {
            return null;
        }
        // List<String> addresslist = GetAddressList(address, count, 16);
        byte[] bytes = null;
        try {
            bytes = s7Plc.readByte(address, count);
        } catch (Exception e) {
            // 处理异常
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());
        } finally {
            System.out.println("读取 " + address + " 最终：" + bytes);
        }
        return bytes;
    }

    /**
     * 按指定的地址 按bit位 0 flase 1 true 读取结果
     *
     * @param addresslist 地址集
     * @return Boolean结果
     */
    public List<Boolean> readBits(List<String> addresslist) {
        if (s7Plc == null) {
            return null;
        }
        List<Boolean> values = new ArrayList<>();
        try {
            values = s7Plc.readBoolean(addresslist);
        } catch (Exception e) {
            // 处理异常
            s7Plc.hotRestart();
        } finally {
            System.out.println("读取 " + addresslist + " 最终：" + values);
        }
        return values;
    }

   /* //读取不连续地址bit
    public List<Boolean> readBits(List<String> addressList) {
        if (s7Plc == null || addressList.isEmpty()) {
            return null;
        }

        List<Boolean> values = new ArrayList<>();
        for (String address : addressList) {
            try {
                boolean value = s7Plc.readBoolean(address);
                values.add(value);
            } catch (Exception e) {
                // 处理异常
                System.out.println("读取 " + address + " 失败：" + e.getMessage());
            }
        }

        return values;
    }*/


    public String readString(String address) {
        if (s7Plc == null) {
            return null;
        }
        String result = null;
        try {
            result = s7Plc.readString(address);
        } catch (Exception e) {
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());
        } finally {
            System.out.println("读取 " + address + " 最终：" + result);
        }
        return result;
    }


    public Long readtime(String address) {
        if (s7Plc == null) {
            return null;
        }
        Long result = null;
        try {
            result = s7Plc.readTime(address);
        } catch (Exception e) {
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());
        } finally {
            System.out.println("读取 " + address + " 最终：" + result);
        }
        return result;
    }


    public boolean writetime(String address, long datas) {
        if (s7Plc == null) {
            return false;
        }
        boolean result = false;
        int tryCount = 2;
        do {
            try {
                // 将数据写入单个地址
                s7Plc.writeTime(address, datas);
                result = true;
            } catch (Exception ex) {
                reStartS7client();
            } finally {
                tryCount -= 1;
            }
        }
        while (!result && tryCount > 0);
        return result;
    }


    /**
     * 从指定的地址开始 连续按bit位读取
     *
     * @param address 地址
     * @param count   长度
     * @return Boolean结果
     */
    public List<Boolean> readBits(String address, int count) {
        if (s7Plc == null) {
            return null;
        }
        List<Boolean> values = new ArrayList<>();
        List<String> addresslist = getAddressList(address, count, 1);
        try {
            values = s7Plc.readBoolean(addresslist);
        } catch (Exception e) {
            s7Plc.hotRestart();
            System.out.println("读取 " + address + " 失败：" + e.getMessage());
        } finally {
            System.out.println("读取 " + address + " 最终：" + values);
        }
        return values;
    }

    private List<String> getAddressList(String address, int count, int addedbit) {
        List<String> addresslist = new ArrayList<String>();

        String[] stringdatas = address.trim().split("\\.");
        if (stringdatas.length < 2 || !address.startsWith("DB")) {
            return null;
        }
        int dbwindex = 0;
        int bitindex = 0;
        if (stringdatas.length == 2) {
            dbwindex = Integer.parseInt(stringdatas[1]);
        } else if (stringdatas.length == 3) {
            dbwindex = Integer.parseInt(stringdatas[1]);
            bitindex = Integer.parseInt(stringdatas[2]);
        } else {
            return null;
        }

        addresslist.add(address);
        for (int i = 0; i < count - 1; i++) {

            int bitcurrent = bitindex + addedbit;
            if (bitcurrent > 7) {
                dbwindex += bitcurrent / 8;
                bitindex = 0;
            } else {
                bitindex = bitcurrent;
            }

            String endstr = stringdatas.length == 3 ? "." + bitindex : "";
            addresslist.add(stringdatas[0] + "." + dbwindex + endstr);
        }
        return addresslist;
    }
}
