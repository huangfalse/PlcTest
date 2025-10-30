package com.mes.connect.entity;

import lombok.Data;

/**
 * PLC通讯参数
 *
 * @author yzx
 * @version 1.0
 */
@Data
public class Parameters {
    private String serialNumber;
    private String content;
    private String codeId;
    private String plcDataType;
    private int fontLocation;
    private int length;
    private String ratio;
    private String scale;
    private String address;
    private byte[] readByte;
    private Object readValue;
    private String remarks;


}
