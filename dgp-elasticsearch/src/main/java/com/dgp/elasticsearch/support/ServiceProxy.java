package com.dgp.elasticsearch.support;

import com.dgp.elasticsearch.connection.EsDataSource;
import com.dgp.elasticsearch.connection.EsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

@Slf4j
public class ServiceProxy<T> implements InvocationHandler {

	private T target;

	private EsDataSource dataSource;

	public ServiceProxy(T target, EsDataSource dataSource) {
		this.target = target;
		this.dataSource = dataSource;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			EsRequest esRequest = new EsRequest(method, args, (Class<?>) target, dataSource);
			return esRequest.invoke();
		} catch (Exception e) {
			log.error("es execute error:{}", e);
			throw e;
		}
	}

}
