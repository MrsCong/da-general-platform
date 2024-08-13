package com.dgp.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liushengping
 * @since 2021/9/15
 */
@Slf4j
public class BeanCopierUtil {

    private static final int CACHE_SIZE = 1 << 7;

    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>(
            CACHE_SIZE);

    // ----------------------------------简单对象转换方法

    /**
     * 简单对象转换
     *
     * @param source source
     * @param target target
     */
    public static void copyProperties(Object source, Object target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    /**
     * 简单对象转换
     *
     * @param source      source
     * @param targetClass target
     * @return target
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        try {
            T t = targetClass.newInstance();
            copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("object source {}  copy to  targetClass is error:{}", source.getClass(), e);
            return null;
        }

    }

    /**
     * 将源list转换成targetClass，并生成list
     *
     * @param sourceList  源list
     * @param targetClass 目标类
     * @return target list
     */
    public static <T> List<T> copyPropertiesOfList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<>(sourceList.size());
        sourceList.stream().forEach(source -> {
            T target;
            try {
                target = targetClass.newInstance();
                copyProperties(source, target);
                resultList.add(target);
            } catch (Exception e) {
                log.error("{} to {} is error:{} ", source.getClass(), targetClass, e);
                throw new RuntimeException(e);
            }

        });
        return resultList;
    }

    // ----------------------------------------私有通用方法

    /**
     * 获取BeanCopier 带本地缓存逻辑
     *
     * @param sourceClass source
     * @param targetClass target
     * @return beanCopier
     */
    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass) {
        String key = generateKey(sourceClass, targetClass);
        BeanCopier copier;
        if (!BEAN_COPIER_CACHE.containsKey(key)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIER_CACHE.put(key, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(key);
        }
        return copier;
    }

    /**
     * 生成Key策略
     *
     * @param sourceClass sourceClass
     * @param targetClass targetClass
     * @return key
     */
    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getName() + targetClass.getName();
    }

}
