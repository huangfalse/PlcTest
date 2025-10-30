package com.mes.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * plc型号表
 * </p>
 *
 * @author yzx
 * @since 2025-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlcType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 备注
     */
    private String remarks;


}
