package com.dgp.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "es")
public class EsConfigProperties {

    private String url;

    private String packages;

    private String dslPath;

    private String user;

    private String passWord;

    // 最大连接数
    private Integer maxTotal = 200;

    // 最大路由数
    private Integer maxRoute = 100;

    /**
     * socket超时时间，单位毫秒
     */
    private int socketTimeout = 500000;
    /**
     * 连接超时时间，单位毫秒
     */
    private int connectTimeout = 5000;
    /**
     * 连接请求超时时间，单位毫秒
     */
    private int connectionRequestTimeout = 5000;
    /**
     * 证书文件路径
     */
    private String certFilePath;
    /**
     * 证书密码
     */
    private String certPassword;
    /**
     * 证书格式 X509、PKCS12、JKS
     */
    private String certType;

}
