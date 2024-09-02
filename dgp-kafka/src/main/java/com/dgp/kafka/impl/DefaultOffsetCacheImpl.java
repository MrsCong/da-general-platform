package com.dgp.kafka.impl;


import com.dgp.common.cache.Cache;

import java.util.HashMap;
import java.util.Map;

public class DefaultOffsetCacheImpl implements Cache {

    public static final Map<String, Map<String, String>> offsetMap = new HashMap<String, Map<String, String>>();

    @Override
    public void put(String key, String field, String offset) {
        Map<String, String> valMap = offsetMap.get(key);
        if (valMap == null) {
            valMap = new HashMap<String, String>();
        }
        valMap.put(field, offset);
        offsetMap.put(key, valMap);

    }

    @Override
    public String get(String key, String field) {
        Map<String, String> valMap = offsetMap.get(key);
        if (valMap == null) {
            return null;
        }
        return valMap.get(field);
    }

}
