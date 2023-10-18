package com.bank.exercise.pdfupload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongExtensionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongExtensionException(String message) {
		super(message);
	}
}
