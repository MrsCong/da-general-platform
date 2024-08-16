package com.dgp.core.http.response;

import cn.hutool.core.date.DateTime;
import com.dgp.core.http.code.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BaseResponse {

    @ApiModelProperty(value = "状态码", required = true)
    private Integer code = 200;

    @ApiModelProperty(value = "提示信息")
    private String message = "SUCCESS";

    @ApiModelProperty(value = "响应时间", required = true)
    private String timestamp = DateTime.now().toString();

    public int getCode() {
        return code;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse() {
    }

    public BaseResponse code(StatusCode code) {
        this.code = code.getCode();
        return this;
    }

    public BaseResponse message(String message) {
        this.message = message;
        return this;
    }

}
