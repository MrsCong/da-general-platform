package com.dgp.common.exception;


import com.dgp.common.code.BaseStatusCode;
import com.dgp.common.code.StatusCode;

public class RsaException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RsaException() {

        super(BaseStatusCode.INTERNAL_SERVER_ERROR);
    }

    public RsaException(StatusCode code, Throwable cause) {
        super(code, cause);
    }

    public RsaException(Throwable cause) {
        super(cause);
    }

}
