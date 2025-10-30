package com.mes.tools;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.FeatureDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EntityTool class
 *
 * @author yzx
 * @date 2025/8/4
 */
public class EntityTool {
    /**
     * 批量将源列表转换为目标实体列表（忽略null值）
     * @param sourceList 源对象列表
     * @param targetClass 目标实体类的Class对象
     * @param <S> 源对象类型
     * @param <T> 目标实体类型
     * @return 转换后的目标实体列表
     */
    public static <S, T> List<T> batchConvert(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            // 空列表直接返回
            return new ArrayList<>();
        }

        // 逐个转换
        return sourceList.stream()
                .map(source -> convertSingle(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 单个对象转换（复用之前的非null复制逻辑）
     * @param source 源对象
     * @param targetClass 目标实体类的Class对象
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     * @return 转换后的目标实体
     */
    public static <S, T> T convertSingle(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyNonNullProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 复制源对象的非null属性到目标对象
     * @param source  源对象
     * @param target 目标实体类
     */
    private static void copyNonNullProperties(Object source, Object target) {
        BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        Stream.of(sourceWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> !"class".equals(name))
                .forEach(name -> {
                    try {
                        Object value = sourceWrapper.getPropertyValue(name);
                        if (value != null && targetWrapper.isWritableProperty(name)) {
                            targetWrapper.setPropertyValue(name, value);
                        }
                    } catch (Exception e) {
                        // 可添加日志：忽略属性复制异常
                    }
                });
    }
}
