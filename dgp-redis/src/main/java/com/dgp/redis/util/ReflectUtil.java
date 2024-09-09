package com.dgp.redis.util;

import cn.hutool.json.JSONConverter;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil {

    public static <T> String transfer(T body) {
        if (body instanceof Number || body instanceof Boolean || body instanceof Character
                || body instanceof String) {
            return body.toString();
        } else {
            return JSONUtil.toJsonStr(body);
        }
    }

    /**
     * 转换对象类型方法
     *
     * @param object source
     * @param clazz  target
     * @param <T>    T
     * @return t
     */
    public static <T> T cast(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        if (object instanceof String) {
            if (clazz.isAssignableFrom(Integer.class)) {
                return clazz.cast(Integer.valueOf((String) object));
            } else if (clazz.isAssignableFrom(Double.class)) {
                return clazz.cast(Double.valueOf((String) object));
            } else if (clazz.isAssignableFrom(Long.class)) {
                return clazz.cast(Long.valueOf((String) object));
            } else if (clazz.isAssignableFrom(Boolean.class)) {
                return clazz.cast(Boolean.valueOf((String) object));
            } else if (clazz.isAssignableFrom(Character.class)) {
                return clazz.cast(((String) object).charAt(0));
            } else if (clazz.isAssignableFrom(String.class)) {
                return clazz.cast(object);
            } else {
                return JSONUtil.toBean((String) object, clazz);
            }
        }
        Method convertMethod = ReflectUtil.getMethodByName(JSONConverter.class, "jsonConvert");
        return ReflectUtil.invokeStatic(convertMethod, clazz, object, true);
    }

}
