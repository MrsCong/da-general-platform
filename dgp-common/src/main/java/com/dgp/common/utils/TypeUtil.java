package com.dgp.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 类型工具类
 *
 * @author Liushengping
 * @since 2021/9/9
 */
public class TypeUtil {

    public static HashMap<Class, Object> CACHED_COMMON_TYPE_MAP = new HashMap<>(32);

    public static List<Class<?>> CACHED_COMMON_TYPE_LIST;

    private static final Class<?>[] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
            double.class, Double.class, float.class, Float.class, int.class, Integer.class,
            long.class, Long.class, short.class, Short.class, String.class, Object.class};

    static {
        CACHED_COMMON_TYPE_LIST = Arrays.asList(CACHED_COMMON_TYPES);
    }

    // ----------------------------methods--------------------------

    /**
     * 判断o是否是基本类型
     *
     * @param source source
     * @return true：属于基本类型；false：不属于
     */
    public static boolean instanceofBasicType(Object source) {
        return TypeUtil.CACHED_COMMON_TYPE_LIST.contains(source.getClass());
    }

}
