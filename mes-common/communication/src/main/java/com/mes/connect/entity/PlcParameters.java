package com.mes.connect.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PLC通讯参数 集合
 *
 * @author yzx
 * @version 1.0
 */
@Data
public class PlcParameters {
    private List<Parameters> parameters;
    private Map<String, Parameters> map;

    public void initialization() {
        this.map = parameters.stream().collect(Collectors.toMap(Parameters::getCodeId, item -> item));
    }

}
