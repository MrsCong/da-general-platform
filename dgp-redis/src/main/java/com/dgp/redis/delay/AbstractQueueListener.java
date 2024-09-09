package com.dgp.redis.delay;

import com.dgp.redis.api.IQueueListener;
import com.dgp.redis.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
@SuppressWarnings("ALL")
public abstract class AbstractQueueListener<T> implements IQueueListener {

    @Override
    public void consumer(String body) {
        //获取泛型
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();

        //jsonStr to real type
        Class<T> clazz = (Class<T>) types[0];
        T message = ReflectUtil.cast(body, clazz);
        handle(message);
    }

    protected abstract void handle(T body);
}
