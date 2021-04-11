package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityNotParsableException extends RuntimeException {

	private static final long serialVersionUID = 6016087953273892292L;

	public EntityNotParsableException() {
		super();
	}

	public EntityNotParsableException(String message) {
		super(message);
	}
}
