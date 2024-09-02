package com.dgp.common.context;

import com.dgp.common.utils.BeanMapUtil;

import java.util.HashMap;
import java.util.Map;

public class BaseThreadLocal {

    public static InheritableThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static void set(Object obj) {
        Map<String, Object> mapValue = BeanMapUtil.getBean2Map(obj);
        Map<String, Object> localMap = threadLocal.get();
        if (localMap == null) {
            localMap = new HashMap<String, Object>();
            threadLocal.set(localMap);
        }
        localMap.putAll(mapValue);
    }

    public static Object get(String key) {
        Map<String, Object> localMap = threadLocal.get();
        if (localMap == null) {
            localMap = new HashMap<String, Object>();
            threadLocal.set(localMap);
        }
        return localMap.get(key);
    }

    public static String getString(String key) {
        return String.valueOf(get(key));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(String key, Class<T> t) {
        Object obj = get(key);
        return (T) obj;
    }

    public static void set(Map<String, Object> valueMap) {
        Map<String, Object> localMap = threadLocal.get();
        if (localMap == null) {
            localMap = new HashMap<String, Object>();
            threadLocal.set(localMap);
        }
        localMap.putAll(valueMap);
    }

    public static void remove() {
        threadLocal.remove();
    }

}
