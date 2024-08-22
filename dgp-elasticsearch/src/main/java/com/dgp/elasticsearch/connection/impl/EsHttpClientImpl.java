package com.dgp.elasticsearch.connection.impl;

import com.alibaba.fastjson.JSON;
import com.dgp.common.exception.ESQueryException;
import com.dgp.elasticsearch.config.EsConfigProperties;
import com.dgp.elasticsearch.connection.EsHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

@Slf4j
public class EsHttpClientImpl implements EsHttpClient {

	public final static String CHARSET = "UTF-8";
	public final static String CONTENT_TYPE_JSON = "application/json";

	private CloseableHttpClient httpClient;
	private EsConfigProperties esConfig;

	public EsHttpClientImpl(CloseableHttpClient httpClient, EsConfigProperties esConfig) {

		this.httpClient = httpClient;
		this.esConfig = esConfig;

	}

	public static EsHttpClientImpl builder(CloseableHttpClient httpClient, EsConfigProperties esConfig) {

		return new EsHttpClientImpl(httpClient, esConfig);
	}

	@Override
	public String executePostJson(String dsl, String path) {
		String url = customUrl(path);
		try {
			String result = postJson(url, dsl);
			return result;
		} catch (Exception e) {
			throw new ESQueryException(e);
		}

	}

	@Override
	public String executeDelete(String path) {
		String url = customUrl(path);
		try {
			String result = delete(url);
			return result;
		} catch (Exception e) {
			throw new ESQueryException(e);
		}
	}


	private String delete(String url) throws Exception {
		CloseableHttpResponse response = null;
		try {
			HttpDelete request = new HttpDelete(url);
			request.addHeader(HttpHeaders.CONTENT_ENCODING, CHARSET);
			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			return result;
		} finally {
			// 包含 EntityUtils.consume(entity);
			HttpClientUtils.closeQuietly(response);
		}
	}

	@Override
	public String customUrl(String path) {
		if (StringUtils.isEmpty(path)) {
			return esConfig.getUrl();
		} else if (path.startsWith("http")) {
			return path;
		}

		return esConfig.getUrl() + path;
	}

	@Override
	public String executeGet(String path) {
		String url = customUrl(path);
		try {
			return get(url);
		} catch (Exception e) {
			throw new ESQueryException(e);
		}
	}

	/**
	 * 关闭httpClient连接
	 *
	 * @param httpClient
	 */
	public static void closeHttpClient(CloseableHttpClient httpClient) {
		HttpClientUtils.closeQuietly(httpClient);
	}

	/**
	 * 发送GET请求
	 * @return
	 * @throws Exception
	 */
	public String get(String url) throws Exception {
		CloseableHttpResponse response = null;
		try {
			HttpGet request = new HttpGet(url);
			request.addHeader(HttpHeaders.CONTENT_ENCODING, CHARSET);
			response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			return result;
		} finally {
			// 包含 EntityUtils.consume(entity);
			HttpClientUtils.closeQuietly(response);
		}
	}

	/**
	 * 执行一个HTTP POST JSON请求
	 *
	 * @return
	 * @throws Exception
	 */
	public String postJson(String url, Object requestObject) throws Exception {
		String reqJson = null;
		if (requestObject != null) {
			if (requestObject instanceof String) {
				reqJson = (String) requestObject;
			} else {
				reqJson = JSON.toJSONString(requestObject);
			}
		}
		String ret = post(url, reqJson, CONTENT_TYPE_JSON);
		return ret;
	}

	/**
	 * 执行一个HTTP POST JSON请求
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String post(String url, String data, String contentType) throws Exception {
		CloseableHttpResponse response = null;
		HttpPost request = null;
		try {
			request = new HttpPost(url);
			if (StringUtils.isNotEmpty(contentType)) {
				request.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
			}

			request.addHeader(HttpHeaders.CONTENT_ENCODING, CHARSET);
			if (StringUtils.isNotEmpty(data)) {
				request.setEntity(new StringEntity(data, CHARSET));
			}
			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			return result;
		} finally {
			HttpClientUtils.closeQuietly(response);
		}
	}

}
