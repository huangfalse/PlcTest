package com.mes.job.config;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.mes.s7.enhanced.EnhancedS7Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : zhoush
 * @Date: 2025/10/30
 * @Description: PLC发送模块S7配置
 */
@Configuration
public class S7PlcSendConfig {

    @Bean
    public EnhancedS7Serializer s7Serializer() {
        // 这里使用一个默认的PLC地址，实际应用中应该从配置文件读取
        S7PLC s7Plc = new S7PLC(EPlcType.S1200, "192.168.10.21");
        return EnhancedS7Serializer.newInstance(s7Plc);
    }
}