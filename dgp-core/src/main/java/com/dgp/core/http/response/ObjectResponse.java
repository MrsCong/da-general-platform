package com.dgp.core.http.response;

import cn.hutool.json.JSONObject;
import com.dgp.core.http.code.BaseStatusCode;
import com.dgp.core.http.code.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("请求响应对象")
public class ObjectResponse<T> extends BaseResponse {

    @ApiModelProperty(value = "响应数据")
    private T data;

    public String toString() {
        return new JSONObject(this).toString();
    }

    public static <T> ObjectResponse<T> success(T data, String message) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> ObjectResponse<T> success(T data) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setData(data);
        result.setMessage(BaseStatusCode.SUCCESS.getMessage());
        return result;
    }

    public static ObjectResponse<String> success() {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setMessage(BaseStatusCode.SUCCESS.getMessage());
        return result;
    }

    /**
     * 无数据失败返回
     *
     * @param code    返回码
     * @param message 消息
     * @return ignore
     */
    public static ObjectResponse<String> failed(StatusCode code, String message) {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(code.getMessage());
        result.setMessage(message);

        return result;
    }

    public static ObjectResponse<String> failed(StatusCode code) {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(code.getMessage());
        result.setMessage(code.getMessage());

        return result;
    }

    public static <T> ObjectResponse<T> failed(StatusCode code, T data, String message) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> ObjectResponse<T> failed(StatusCode code, T data) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(data);
        result.setMessage(code.getMessage());
        return result;
    }
}