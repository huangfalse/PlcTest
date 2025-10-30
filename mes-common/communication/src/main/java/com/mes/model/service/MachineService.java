package com.mes.model.service;

import com.github.yulichang.base.MPJBaseService;
import com.mes.model.entity.Machine;

import java.util.List;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author wu
 * @since 2024-08-28
 */
public interface MachineService extends MPJBaseService<Machine> {
    /**
     * 查找设备 配置信息
     * @return
     */
    List<Machine> getMachineConfig();
}
