package com.dgp.core.http.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseStatusCode implements StatusCode {

    /**
     * 成功
     */
    SUCCESS(200, "请求已经成功处理"),
    /**
     * 服务内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    GATEWAY_TIMEOUT(504, "上游服务器超时"),
    BAD_REQUEST(400, "请求错误，请修正请求"),
    NOT_FOUND(404, "资源未找到"),

    ES_BAD_ACTION(4401, "请求错误，未设置action"),

    PARAM_INVALID(120401, "请求参数无效"),

    SYSTEM_SERVICE_UNAVAILABLE(990503, "系统维护中，服务暂不可用！"),

    RSA_ERROR(5001, "rsa加解密失败"),

    UNAUTHORIZED(401, "未认证（签名错误）"),

    PERMISSION_ERROR(403, "没有访问权限"),

    IMPORT_ERROR(201, "//导入的数据有错误");


    private final Integer code;

    private final String message;

}
