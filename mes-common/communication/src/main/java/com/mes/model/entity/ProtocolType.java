package com.mes.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 协议类型表
 * </p>
 *
 * @author yzx
 * @since 2025-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProtocolType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 协议名称
     */
    private String name;

    /**
     * 备注
     */
    private String remarks;


}
