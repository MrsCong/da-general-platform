package com.dgp.core.http.code;

import com.dgp.core.exception.BaseException;

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
