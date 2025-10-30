package com.mes.s7.enhanced;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author : zhoush
 * @Date: 2025/7/31 9:21
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "s7")
public class S7AddressProperties {
    private Map<String, String> address;

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }
}
