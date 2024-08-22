package com.dgp.common.utils;

import com.dgp.common.exception.GsonException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder().setDateFormat(DateUtil.FORMAT_YMDHMS)
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(), new MapTypeAdapter()).create();

    /**
     * 字符串转JsonObject
     */
    public static JsonObject str2JsonObject(String jsonStr) {
        return GSON.fromJson(jsonStr, JsonElement.class).getAsJsonObject();
    }

    /**
     * 对象转字符串
     */
    public static String obj2String(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 字符换转换对象
     */
    public static <T> T str2Object(String jsonString, Class<T> clazz) {
        try {
            return GSON.fromJson(jsonString, clazz);
        } catch (JsonSyntaxException e) {
            log.warn("Gson string to object error, string : {} to object type {} {}", jsonString,
                    clazz.getCanonicalName(), e);
            throw new GsonException("GSON对象转换失败", e);
        }
    }

    /**
     * 字符串转换对象带泛型
     */
    public static <T> T str2ObjectGenericity(String jsonString, Class<?> cls, Class<?> t) {
        try {
            return GSON.fromJson(jsonString, new ObjectTypeImpl(cls, t));
        } catch (JsonSyntaxException e) {
            log.warn("Gson string to object error, string : {} to object type {} {}", jsonString,
                    cls.getCanonicalName(), e);
            throw new GsonException("GSON对象转换失败", e);
        }
    }

    /**
     * 对象转对象
     */
    public static <T> T obj2Object(Object s, Class<T> t) {
        try {

            return GSON.fromJson(obj2String(s), t);
        } catch (JsonSyntaxException e) {
            log.warn("Gson string to object error, string : {} to object type {}", s, t);
            throw new GsonException("GSON对象转换失败", e);
        }
    }

    /**
     * list对象数组转list对象数组
     */
    public static <T> List<T> objList2ObjectList(Object list, Class<T> t) {
        try {

            return str2ObjectList(obj2String(list), t);
        } catch (JsonSyntaxException e) {
            log.warn("Gson string to object error, string : {} to object type {}", obj2String(list), t);
            throw new GsonException("GSON对象转换失败", e);
        }
    }

    /**
     * json字符串转list对象数组
     */
    public static <T> List<T> str2ObjectList(String jsonString, Class<T> clazz) {
        try {
            return GSON.fromJson(jsonString, new ListTTypeImpl(clazz));
        } catch (JsonSyntaxException e) {
            log.warn("Gson string to object list error, string : {} to object type {}", jsonString,
                    clazz.getCanonicalName());
            throw new GsonException("GSON对象转换失败", e);
        }
    }

    /**
     * 字符串List<Map<String, T>>
     *
     * @param <T>
     * @param gsonString
     * @param t
     * @return
     */
    public static <T> List<Map<String, T>> str2ListMaps(String gsonString, Class<T> t) {
        List<Map<String, T>> list = new ArrayList<Map<String, T>>();
        ArrayList<Map<String, T>> temp = GSON.fromJson(gsonString,
                TypeToken.getParameterized(ArrayList.class, Map.class, t).getType());
        temp.forEach(tempMap -> {
            Map<String, T> oneMap = new HashMap<String, T>();
            tempMap.forEach((k, v) -> {
                T tempt = GsonUtil.str2Object(GsonUtil.obj2String(v), t);
                oneMap.put(k, tempt);
            });
            list.add(oneMap);
        });
        return list;
    }

    /**
     * 字符串转Map<String, T>
     *
     * @param <T>
     * @param gsonString
     * @param c
     * @return
     */
    public static <T> Map<String, T> str2Map(String gsonString, Class<T> c) {
        Map<String, T> map = null;
        map = GSON.fromJson(gsonString, TypeToken.getParameterized(Map.class, String.class, c).getType());
        return map;
    }

    private static class ListTTypeImpl implements ParameterizedType {

        Class clazz;

        ListTTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    private static class ObjectTypeImpl implements ParameterizedType {

        Class clazz;
        Class innert;

        ObjectTypeImpl(Class object, Class t) {
            clazz = object;
            innert = t;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{innert};
        }

        @Override
        public Type getRawType() {
            return clazz;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        return lngNum;
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // TODO Auto-generated method stub

        }

    }

}
