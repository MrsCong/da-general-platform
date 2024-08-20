package com.dgp.common.code;


import com.dgp.common.exception.BaseException;

/**
 * 异常状态码和信息
 */
public interface StatusCode {

    Integer getCode();

    String getMessage();

    default BaseException toException() {
        return new BaseException(this);
    }

}
