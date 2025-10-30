package com.mes.utils;

import com.github.xingshuangs.iot.protocol.s7.serializer.S7Parameter;
import com.github.xingshuangs.iot.protocol.s7.serializer.S7Variable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/9/3 15:43
 * @Description:
 */
public class S7ParameterUtils {


    /**
     * 获取单个地址信息
     *
     * @param targetClass
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<S7Parameter> getSingleS7ParameterList(Class<T> targetClass, String fieldName, Object value) {
        List<S7Parameter> s7ParameterList = new ArrayList<>();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            final S7Variable s7Variable = field.getAnnotation(S7Variable.class);
            if (s7Variable == null) {
                return new ArrayList<>();
            }
            s7ParameterList.add(new S7Parameter(s7Variable.address(), s7Variable.type(), s7Variable.count(), value));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return s7ParameterList;
    }

    /**
     * 获取单个地址信息
     *
     * @param targetClass
     * @param dbArea
     * @param beginIndex
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<S7Parameter> getSingleS7ParameterList(Class<T> targetClass, String dbArea, Integer beginIndex, String fieldName, Object value) {
        List<S7Parameter> s7ParameterList = new ArrayList<>();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            final S7Variable s7Variable = field.getAnnotation(S7Variable.class);
            if (s7Variable == null) {
                return new ArrayList<>();
            }
            s7ParameterList.add(new S7Parameter(dbArea + beginIndex + s7Variable.address(), s7Variable.type(), s7Variable.count(), value));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return s7ParameterList;
    }

    /**
     * 获取多个地址信息：注意value的值要与字段名一致
     *
     * @param targetClass
     * @param fieldNameList
     * @param values
     * @param <T>
     * @return
     */
    public static <T> List<S7Parameter> getS7ParameterList(Class<T> targetClass, List<String> fieldNameList, List<Object> values) {
        List<S7Parameter> s7ParameterList = new ArrayList<>();
        for (int i = 0; i < fieldNameList.size(); i++) {
            try {
                Field field = targetClass.getDeclaredField(fieldNameList.get(i));
                final S7Variable s7Variable = field.getAnnotation(S7Variable.class);
                if (s7Variable == null) {
                    continue;
                }
                s7ParameterList.add(new S7Parameter(s7Variable.address(), s7Variable.type(), s7Variable.count(), values.get(i)));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return s7ParameterList;
    }


    /**
     * 获取多个地址信息：注意value的值要与字段名一致
     *
     * @param targetClass
     * @param dbArea
     * @param beginIndex
     * @param fieldNameList
     * @param values
     * @param <T>
     * @return
     */
    public static <T> List<S7Parameter> getS7ParameterList(Class<T> targetClass, String dbArea, Integer beginIndex, List<String> fieldNameList, List<Object> values) {
        List<S7Parameter> s7ParameterList = new ArrayList<>();
        for (int i = 0; i < fieldNameList.size(); i++) {
            try {
                Field field = targetClass.getDeclaredField(fieldNameList.get(i));
                final S7Variable s7Variable = field.getAnnotation(S7Variable.class);
                if (s7Variable == null) {
                    continue;
                }
                s7ParameterList.add(new S7Parameter(dbArea + beginIndex + s7Variable.address(), s7Variable.type(), s7Variable.count(), values.get(i)));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return s7ParameterList;
    }
}
