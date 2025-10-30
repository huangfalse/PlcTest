package com.mes.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author wu
 * @since 2024-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Machine implements Serializable {

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
     * 协议类型ID 可以直接添加关联对象
     */
    @TableField(exist = false, fill = FieldFill.INSERT_UPDATE)
    private ProtocolType protocolType;
    private Long protocolTypeId;
    /**
     * PLC类型
     */
    @TableField(exist = false)
    private PlcType plcType;
    private String plcTypeId;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

    /**
     * 参数文件路径
     */
    private String fileUrl;
    /**
     * 参数文件
     */
    private String machineFile;

    /**
     * 逻辑文件
     */
    private String logicFile;

    /**
     * 开关
     */
    private String open;

    public String getMachineFile() {
        return this.fileUrl + "\\" + this.machineFile;
    }

    public String getLogicFile() {
        return this.fileUrl + "\\" + this.logicFile;
    }

}
