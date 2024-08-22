package com.dgp.common.exception;

import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.code.StatusCode;

public class ESQueryException extends BaseException {


    private static final long serialVersionUID = 3943257531522347260L;

    public ESQueryException() {
    }

    public ESQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ESQueryException(String message) {
        super(message, BaseStatusCode.INTERNAL_SERVER_ERROR);
    }

    public ESQueryException(Throwable cause) {
        super(cause);
    }

    public ESQueryException(StatusCode code) {
        super(code.getMessage(), code);
    }

    public ESQueryException(String message, StatusCode code) {
        super(message, code);
    }

}
