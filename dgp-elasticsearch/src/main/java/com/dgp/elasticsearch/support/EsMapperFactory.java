package com.dgp.elasticsearch.support;

import com.dgp.elasticsearch.connection.EsDataSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

public class EsMapperFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceType;
    
    @Autowired
    private EsDataSource dataSource;

    public EsMapperFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() {
        InvocationHandler handler = new ServiceProxy(interfaceType,dataSource);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
