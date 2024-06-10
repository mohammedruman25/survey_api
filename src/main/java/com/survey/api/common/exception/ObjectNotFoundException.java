package com.survey.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8238431409558935675L;

//	public ObjectNotFoundException() {
//	}

	public ObjectNotFoundException(String message) {
		super(message);
	}

//	public ObjectNotFoundException(Throwable cause) {
//		super(cause);
//	}
//
//	public ObjectNotFoundException(String message, Throwable cause) {
//		super(message, cause);
//	}
//
//	public ObjectNotFoundException(String message, Throwable cause, boolean enableSuppression,
//			boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}

}
