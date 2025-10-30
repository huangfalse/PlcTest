package com.mes.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.mes.model.entity.Machine;
import com.mes.model.entity.PlcType;
import com.mes.model.entity.ProtocolType;
import com.mes.model.mapper.MachineMapper;
import com.mes.model.mapper.PlcTypeMapper;
import com.mes.model.mapper.ProtocolTypeMapper;
import com.mes.model.service.MachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author yanzhixin
 * @since 2024-09-03
 */
@Slf4j
@Service
public class MachineServiceImpl extends MPJBaseServiceImpl<MachineMapper, Machine> implements MachineService {
    @Autowired
    private PlcTypeMapper plcTypeMapper;
    @Autowired
    private ProtocolTypeMapper protocolTypeMapper;

    @Override
    public List<Machine> getMachineConfig() {
        List<Machine> machineConfig = baseMapper.selectList(null);
        for (int i = 0; i < machineConfig.size(); i++) {
            PlcType plcType = plcTypeMapper.selectOne(new QueryWrapper<PlcType>().lambda()
                    .eq(PlcType::getId, machineConfig.get(i).getPlcTypeId()));
            ProtocolType protocolType = protocolTypeMapper.selectOne(new QueryWrapper<ProtocolType>().lambda()
                    .eq(ProtocolType::getId, machineConfig.get(i).getProtocolTypeId()));
            machineConfig.get(i).setPlcType(plcType);
            machineConfig.get(i).setProtocolType(protocolType);
        }
        return machineConfig;
    }

}
