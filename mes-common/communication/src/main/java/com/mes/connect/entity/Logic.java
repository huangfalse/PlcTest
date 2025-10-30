package com.mes.connect.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * plc 逻辑项
 * @author yzx
 * @version 1.0
 */
@Data
public class Logic {
    private String codeId;
    private String address;
    private List<String> value;
    private String plcDataType;
    private int length;
    @Getter
    @Setter
    private boolean equals;
}    