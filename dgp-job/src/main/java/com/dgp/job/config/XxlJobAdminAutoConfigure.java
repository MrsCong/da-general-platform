package com.dgp.job.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.dgp.job.http.HttpHeader;
import com.dgp.job.service.XxlJobService;
import com.dgp.job.service.impl.XxlJobServiceImpl;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.IpUtil;
import com.xxl.job.core.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.HttpCookie;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobAdminProperties.class)
public class XxlJobAdminAutoConfigure {

    /**
     * xxl-job-admin 登录请求头
     *
     * @param xxlJobAdminProperties xxl-job-admin 配置
     * @return {@link HttpHeader} 登录请求头
     */
    @Bean("loginHeader")
    @SuppressWarnings("all")
    public HttpHeader httpRequest(XxlJobAdminProperties xxlJobAdminProperties) {
        log.info(">>>>>>>>>>> xxl-job config init. httpRequest");
        String adminUrl = xxlJobAdminProperties.getAdminUrl();
        Assert.notBlank(adminUrl, "请配置adminUrl");

        String userName = xxlJobAdminProperties.getUserName();
        String password = xxlJobAdminProperties.getPassword();
        int connectionTimeOut = xxlJobAdminProperties.getConnectionTimeOut();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        paramMap.put("password", password);

        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpRequest.post(adminUrl + "/login").form(paramMap).timeout(connectionTimeOut).execute();
        } catch (Exception e) {
            Assert.isTrue(false, "链接xxl-job服务失败，请检查xxl-job服务是否运行以及到xxl-job的网络是否通畅。当前链接地址：{}", adminUrl);
        }
        int status = httpResponse.getStatus();
        Assert.isTrue(200 == status, "登录失败,请检查用户名密码是否正确");

        String body = httpResponse.body();
        ReturnT returnT = JSONUtil.toBean(body, ReturnT.class);

        Assert.isTrue(200 == returnT.getCode(), "登录失败,请检查用户名密码是否正确");

        String cookieName = "XXL_JOB_LOGIN_IDENTITY";
        HttpCookie cookie = httpResponse.getCookie(cookieName);
        Assert.notNull(cookie, "没有获取到登录成功的cookie，请检查登录连接或者参数是否正确");

        String headerValue = cookieName + "=" + cookie.getValue();

        return new HttpHeader("Cookie", headerValue);
    }

    /**
     * xxl-job 服务
     *
     * @param loginHeader           {@link HttpHeader} 登录请求头
     * @param xxlJobAdminProperties {@link XxlJobAdminProperties} xxl-job-admin 配置
     * @return {@link XxlJobService} xxl-job-admin 服务
     */
    @Bean
    public XxlJobService xxlJobService(HttpHeader loginHeader, XxlJobAdminProperties xxlJobAdminProperties) {
        log.info(">>>>>>>>>>> xxl-job config init. xxlJobService");
        return new XxlJobServiceImpl(loginHeader, xxlJobAdminProperties);
    }

    /**
     * xxl-job 执行器
     *
     * @param xxlJobAdminProperties {@link XxlJobAdminProperties} xxl-job-admin 配置
     * @return {@link XxlJobSpringExecutor} xxl-job执行器
     * @throws UnknownHostException 未知主机异常
     */
    @Bean
    @SuppressWarnings("all")
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobAdminProperties xxlJobAdminProperties) throws UnknownHostException {
        log.info(">>>>>>>>>>> xxl-job config init. XxlJobSpringExecutor");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        String adminUrl = xxlJobAdminProperties.getAdminUrl();
        Assert.notBlank(adminUrl, "xxl-job服务地址不能为空");
        xxlJobSpringExecutor.setAdminAddresses(adminUrl);

        String appname = xxlJobAdminProperties.getAppname();
        Assert.notBlank(appname, "请配置执行器参数appname");
        xxlJobSpringExecutor.setAppname(appname);


        String address = xxlJobAdminProperties.getAddress();

        String ip = xxlJobAdminProperties.getIp();
        if (StrUtil.isBlank(ip)) {
            ip = IpUtil.getIp();
        }
        xxlJobSpringExecutor.setIp(ip);

        Integer port = xxlJobAdminProperties.getPort();
        if (port == null) {
            port = NetUtil.findAvailablePort(9999);
        }
        xxlJobSpringExecutor.setPort(port);

        if (StrUtil.isBlank(address)) {
            String ipPortAddress = IpUtil.getIpPort(ip, port);
            address = "http://{ip_port}/".replace("{ip_port}", ipPortAddress);
        }
        xxlJobSpringExecutor.setAddress(address);

        String accessToken = xxlJobAdminProperties.getAccessToken();
        if (StrUtil.isNotBlank(accessToken)) {
            xxlJobSpringExecutor.setAccessToken(accessToken);
        }

        String logPath = xxlJobAdminProperties.getLogPath();
        if (StrUtil.isNotBlank(logPath)) {
            xxlJobSpringExecutor.setLogPath(logPath);
        }

        Integer logRetentionDays = xxlJobAdminProperties.getLogRetentionDays();
        if (logRetentionDays != null) {
            xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        }

        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appname, address);
        AdminBiz adminBiz = new AdminBizClient(adminUrl.trim(), accessToken);
        try {
            ReturnT<String> registryResult = adminBiz.registry(registryParam);
            if (registryResult != null && ReturnT.SUCCESS_CODE == registryResult.getCode()) {
                registryResult = ReturnT.SUCCESS;
                log.debug(">>>>>>>>>>> xxl-job registry success, registryParam:{}, registryResult:{}", registryParam, registryResult);
            } else {
                log.info(">>>>>>>>>>> xxl-job registry fail, registryParam:{}, registryResult:{}", registryParam, registryResult);
                Assert.isTrue(false, ">>>>>>>>>>> xxl-job registry fail, registryParam:{}, registryResult:{}", registryParam, registryResult);
            }
        } catch (Exception e) {
            log.info(">>>>>>>>>>> xxl-job registry error, registryParam:{}", registryParam, e);
            Assert.isTrue(false, ">>>>>>>>>>> xxl-job registry error, registryParam:{}", registryParam, e);
        }

        return xxlJobSpringExecutor;
    }

}
