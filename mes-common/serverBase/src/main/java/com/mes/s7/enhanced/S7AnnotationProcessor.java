package com.mes.s7.enhanced;

import com.github.xingshuangs.iot.protocol.s7.serializer.S7Variable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Author : zhoush
 * @Date: 2025/7/31 9:19
 * @Description:
 */
@Component
public class S7AnnotationProcessor implements BeanPostProcessor {
    @Autowired
    private S7AddressProperties addressProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        // 处理类上的注解
        processClassAnnotations(clazz);
        // 处理字段上的注解
        for (Field field : clazz.getDeclaredFields()) {
            processFieldAnnotations(field);
        }
        return bean;
    }

    private void processClassAnnotations(Class<?> clazz) {
        if (clazz.isAnnotationPresent(S7Variable.class)) {
            S7Variable annotation = clazz.getAnnotation(S7Variable.class);
            updateAnnotationValue(annotation, determineAddress(annotation.address()));
        }
    }

    private void processFieldAnnotations(Field field) {
        if (field.isAnnotationPresent(S7Variable.class)) {
            S7Variable annotation = field.getAnnotation(S7Variable.class);
            updateAnnotationValue(annotation, determineAddress(annotation.address()));
        }
    }

    private String determineAddress(String addressKey) {
        // 如果addressKey是配置中的key，则从配置获取
        if (addressProperties.getAddress().containsKey(addressKey)) {
            return addressProperties.getAddress().get(addressKey);
        }

        // 尝试直接解析为数字
        try {
            return addressKey;
        } catch (NumberFormatException e) {
            // 返回默认地址
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    private void updateAnnotationValue(S7Variable annotation, String newAddress) {
        try {
            InvocationHandler handler = Proxy.getInvocationHandler(annotation);
            Field memberValuesField = handler.getClass().getDeclaredField("memberValues");
            memberValuesField.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(handler);
            memberValues.put("address", newAddress);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update S7Variable annotation", e);
        }
    }
}
