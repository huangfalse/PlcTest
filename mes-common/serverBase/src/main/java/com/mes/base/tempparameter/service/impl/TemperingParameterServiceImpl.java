package com.mes.base.tempparameter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.base.tempparameter.mapper.TemperingParameterMapper;
import com.mes.base.tempparameter.service.TemperingParameterService;
import com.mes.tempering.param.TemperingParameter;
import org.springframework.stereotype.Service;

/**
 * 钢化参数配置表(TemperingParameter)表服务实现类
 *
 * @author makejava
 * @since 2025-07-08 16:00:03
 */
@Service("temperingParameterService")
public class TemperingParameterServiceImpl extends ServiceImpl<TemperingParameterMapper, TemperingParameter> implements TemperingParameterService {

}

