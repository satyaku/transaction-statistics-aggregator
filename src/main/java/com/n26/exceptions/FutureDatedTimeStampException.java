package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FutureDatedTimeStampException extends RuntimeException {

	private static final long serialVersionUID = -3564363469649722482L;

	public FutureDatedTimeStampException() {
		super();
	}

	public FutureDatedTimeStampException(String message) {
		super(message);
	}
}
