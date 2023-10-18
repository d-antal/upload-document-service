package com.bank.exercise.pdfupload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PostAlreadyCreatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public PostAlreadyCreatedException(String message) {
		super(message);
	}
}
