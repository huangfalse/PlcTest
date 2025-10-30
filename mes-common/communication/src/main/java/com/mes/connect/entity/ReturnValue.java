package com.mes.connect.entity;

import lombok.Data;

/**
 *
 * plc 返回逻辑
 * @author yzx
 * @version 1.0
 */
@Data
public class ReturnValue {
    private String codeId;
    private String address;
    private boolean fixed;
    private String value;
    private String plcDataType;
    private int length;
}    