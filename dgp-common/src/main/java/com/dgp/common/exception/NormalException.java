package com.dgp.common.exception;


import com.dgp.common.code.StatusCode;

public class NormalException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NormalException() {
	}

	public NormalException(String message) {
		super(message);
	}

	public NormalException(StatusCode code) {
		super(code.getMessage(), code);
	}

	public NormalException(String message, StatusCode code) {
		super(message, code);
	}
}