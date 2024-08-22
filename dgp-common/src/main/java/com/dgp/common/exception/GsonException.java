package com.dgp.common.exception;

import com.dgp.common.code.StatusCode;

public class GsonException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = -5641609351300961360L;

    public GsonException() {
    }

    public GsonException(Throwable cause) {
        super(cause);
    }

    public GsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public GsonException(String message) {
        super(message);
    }

    public GsonException(StatusCode code) {
        super(code.getMessage(), code);
    }

    public GsonException(String message, StatusCode code) {
        super(message, code);
    }

}
