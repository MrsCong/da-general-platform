package com.dgp.elasticsearch.connection.impl;

import com.dgp.elasticsearch.config.EsConfigProperties;
import com.dgp.elasticsearch.connection.EsConnection;
import com.dgp.elasticsearch.connection.EsDataSource;
import com.dgp.elasticsearch.support.DSLConfiguration;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

public class EsDataSourceImpl implements EsDataSource {

	EsConfigProperties esConfig;

	private RequestConfig requestConfig;

	private final DSLConfiguration dslConfiguration;

	private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

	private static HttpRequestRetryHandler httpRequestRetryHandler;

	public EsDataSourceImpl(EsConfigProperties esConfig) {
		this.esConfig = esConfig;
		this.dslConfiguration = new DSLConfiguration(esConfig);
		init();

	}

	private void init() {
		// 将最大连接数增加
		connectionManager.setMaxTotal(esConfig.getMaxTotal());
		// 将每个路由基础的连接增加
		connectionManager.setDefaultMaxPerRoute(esConfig.getMaxRoute());
		HttpHost httpHost = getHttpHost(esConfig.getUrl());
		// 将目标主机的最大连接数增加
		connectionManager.setMaxPerRoute(new HttpRoute(httpHost), esConfig.getMaxRoute());

		RequestConfig.Builder builder = RequestConfig.custom();

		// socket超时，单位毫秒
		if (esConfig.getSocketTimeout() > 0) {
			builder.setSocketTimeout(esConfig.getSocketTimeout());
		}
		// 连接超时时间，单位毫秒
		if (esConfig.getConnectTimeout() > 0) {
			builder.setConnectTimeout(esConfig.getConnectTimeout());
		}
		// 连接请求超时时间，单位毫秒
		if (esConfig.getConnectionRequestTimeout() > 0) {
			builder.setSocketTimeout(esConfig.getConnectionRequestTimeout());
		}

		requestConfig = builder.build();

		// 请求重试处理
		httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// SSL握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

	}

	@Override
	public EsConnection getEsConnection() {

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler).build();
		EsConnectionImpl esConnection = new EsConnectionImpl(EsHttpClientImpl.builder(httpClient, esConfig),
				dslConfiguration);
		return esConnection;
	}

	/**
	 * 获取HttpHost
	 */
	public static HttpHost getHttpHost(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}

		return new HttpHost(hostname, port);
	}

	public void setEsConfig(EsConfigProperties esConfig) {
		this.esConfig = esConfig;
	}

	public DSLConfiguration getDslConfiguration() {
		return dslConfiguration;
	}

}
