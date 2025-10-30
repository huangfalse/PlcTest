package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.PlcAddressMapping;
import org.apache.ibatis.annotations.Mapper;

/**
 * PLC地址映射配置Mapper
 * 
 * @author zhoush
 * @date 2025/10/30
 */
@Mapper
public interface PlcAddressMappingMapper extends BaseMapper<PlcAddressMapping> {
    
}