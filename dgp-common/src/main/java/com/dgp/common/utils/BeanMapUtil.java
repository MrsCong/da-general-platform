package com.dgp.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanMapUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getMap2Bean(@SuppressWarnings("rawtypes") Map map, Class<T> beanClass) {
        Object inst = null;

        try {
            Field[] e = beanClass.getDeclaredFields();
            Constructor<?> constructor1 = beanClass.getConstructor(new Class[0]);
            inst = constructor1.newInstance(new Object[0]);
            int len = e.length;

            for (int i = 0; i < len; ++i) {
                Field field = e[i];
                if (!field.getName().equals("serialVersionUID")) {
                    try {
                        PropertyDescriptor e1 = new PropertyDescriptor(field.getName(),
                                inst.getClass());
                        Method method = e1.getWriteMethod();
                        if (null != map.get(field.getName())) {
                            if ("Long".equals(field.getType().getSimpleName())) {
                                method.invoke(inst,
                                        new Object[]{StringUtils
                                                .isEmpty(map.get(field.getName()).toString()) ? ""
                                                : Long.valueOf(Long.parseLong(
                                                map.get(field.getName()).toString()))});
                            } else if ("Date".equals(field.getType().getSimpleName())) {
                                String dateStr = "";
                                if (!StringUtils.isEmpty(map.get(field.getName()).toString())) {
                                    dateStr = map.get(field.getName()).toString();
                                    if (dateStr.indexOf("T") != -1) {
                                        dateStr = dateStr.replace("T", " ");
                                    }
                                }

                                if (field.getType().getName().indexOf("java.util.Date")
                                        >= 0) {
                                    method.invoke(inst,
                                            new Object[]{StringUtils
                                                    .isEmpty(map.get(field.getName()).toString())
                                                    ? ""
                                                    : DateUtil.reverse2Date(dateStr)});
                                } else {
                                    method.invoke(inst,
                                            new Object[]{StringUtils
                                                    .isEmpty(map.get(field.getName()).toString())
                                                    ? ""
                                                    : DateUtil.reverse2SqlDate(dateStr)});
                                }
                            } else if (!"Integer".equals(field.getType().getSimpleName())
                                    && !"int".equals(field.getType().getSimpleName())) {
                                if ("List".equals(field.getType().getSimpleName())) {
                                    method.invoke(inst, new Object[]{
                                            null == map.get(field.getName()) ? ""
                                                    : (List) map.get(field.getName())});
                                } else if ("Map".equals(field.getType().getSimpleName())) {
                                    method.invoke(inst, new Object[]{
                                            null == map.get(field.getName()) ? ""
                                                    : (Map) map.get(field.getName())});
                                } else if (!"Double".equals(field.getType().getSimpleName())
                                        && !"double".equals(field.getType().getSimpleName())) {
                                    if (!"Float".equals(field.getType().getSimpleName())
                                            && !"float".equals(field.getType().getSimpleName())) {
                                        method.invoke(inst,
                                                new Object[]{
                                                        StringUtils.isEmpty(
                                                                map.get(field.getName()).toString())
                                                                ? ""
                                                                : map.get(field.getName())
                                                                .toString()});
                                    } else {
                                        method.invoke(inst, new Object[]{
                                                Float.valueOf(Float.parseFloat(
                                                        map.get(field.getName()).toString()))});
                                    }
                                } else {
                                    method.invoke(inst, new Object[]{
                                            Double.valueOf(Double.parseDouble(
                                                    map.get(field.getName()).toString()))});
                                }
                            } else {
                                method.invoke(inst,
                                        new Object[]{StringUtils
                                                .isEmpty(map.get(field.getName()).toString()) ? ""
                                                : Integer.valueOf(
                                                Integer.parseInt(map.get(field.getName())
                                                        .toString()))});
                            }
                        }
                    } catch (Exception e1) {
                        log.error(e1.getMessage());
                    }
                }
            }
        } catch (Exception e2) {
            log.error(e2.getMessage());
        }

        return (T) inst;
    }

    public static Map<String, Object> getBean2Map(Object beanObj) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (null == beanObj) {
            return map;
        } else {
            try {
                Class<? extends Object> e = beanObj.getClass();
                Field[] fields = e.getDeclaredFields();
                int len = fields.length;

                for (int i = 0; i < len; ++i) {
                    Field field = fields[i];
                    if (!"serialVersionUID".equals(field.getName())) {
                        try {
                            PropertyDescriptor e1 = new PropertyDescriptor(field.getName(), e);
                            Method method = e1.getReadMethod();
                            Object value = method.invoke(beanObj, new Object[0]);
                            if (null != value) {
                                map.put(field.getName(), value);
                            } else if (!"Long".equals(field.getType().getSimpleName())
                                    && !"Integer".equals(field.getType().getSimpleName())) {
                                map.put(field.getName(), "");
                            } else {
                                map.put(field.getName(), -1);
                            }
                        } catch (Exception e1) {
                            log.error(e1.getMessage(), e1);
                        }
                    }
                }
            } catch (Exception e2) {
                log.error(e2.getMessage(), e2);
            }

            return map;
        }
    }

    public static Map<String, String> getBean2MapStr(Object beanObj) {

        Map<String, Object> map = getBean2Map(beanObj);
        Map<String, String> mapStr = new HashMap<String, String>();
        map.forEach((k, v) -> {
            if (TypeUtil.instanceofBasicType(v)) {
                // 基本类型
                mapStr.put(k, v.toString());
            } else {
                mapStr.put(k, JSONObject.toJSONString(v));
            }
        });

        return mapStr;

    }

}
