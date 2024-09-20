package com.dgp.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.dgp.mybatis.annontion.NullableOnUpdate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MybatisUtil {


    /**
     * 根据实体类字段名获取对应的表字段名
     *
     * @param entityClass 实体类的 Class 对象
     * @param fieldName   实体类的字段名
     * @return 表字段名
     */
    public static String getTableFieldName(Class<?> entityClass, String fieldName) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo == null) {
            throw new IllegalArgumentException("TableInfo not found for entity class: " + entityClass.getName());
        }
        List<TableFieldInfo> tableFieldList = tableInfo.getFieldList();
        for (TableFieldInfo fieldInfo : tableFieldList) {
            if (fieldInfo.getProperty().equals(fieldName)) {
                return fieldInfo.getColumn();
            }
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }

    /**
     * 获取实体类中为null的字段，并设置成null
     *
     * @param bean
     * @return
     */
    public static <T> LambdaUpdateWrapper<T> getNullUpdateWrapper(T bean) {
        if (Objects.isNull(bean)) {
            return null;
        }
        Class<?> clazz = bean.getClass();
        UpdateWrapper<T> uw = new UpdateWrapper<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                NullableOnUpdate annotation = AnnotationUtils.findAnnotation(field, NullableOnUpdate.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    if (field.get(bean) == null) {
                        uw.set(getTableFieldName(clazz, field.getName()), null);
                    }
                }
            }
            if (StringUtils.isBlank(uw.getSqlSet())) {
                return null;
            }
            return uw.lambda();
        } catch (IllegalAccessException e) {
            log.error("Error setting field value", e);
        }
        return null;
    }

}
