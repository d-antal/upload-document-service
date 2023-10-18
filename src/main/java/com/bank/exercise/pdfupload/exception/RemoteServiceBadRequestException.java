package com.bank.exercise.pdfupload.exception;

public class RemoteServiceBadRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteServiceBadRequestException() {
	}

	public RemoteServiceBadRequestException(String message) {
		super(message);
	}

	public RemoteServiceBadRequestException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "RemoteServiceBadRequestException: " + getMessage();
	}

}