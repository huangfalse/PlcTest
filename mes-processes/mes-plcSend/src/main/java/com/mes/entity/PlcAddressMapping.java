package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * PLC地址映射配置表
 * 
 * @author zhoush
 * @date 2025/10/30
 */
@Data
@TableName("plc_address_mapping")
public class PlcAddressMapping implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 项目标识
     */
    private String projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * DB块地址
     */
    private String dbArea;
    
    /**
     * 起始索引
     */
    private int beginIndex;
    
    /**
     * PLC IP地址
     */
    private String plcIp;
    
    /**
     * PLC类型
     */
    private String plcType;
    
    /**
     * 地址映射配置(JSON格式存储)
     * key为字段名，value为地址偏移量
     */
    private String addressMapping;
    
    /**
     * 是否启用(0-禁用,1-启用)
     */
    private int enabled;
    
    /**
     * 备注
     */
    private String remarks;
}