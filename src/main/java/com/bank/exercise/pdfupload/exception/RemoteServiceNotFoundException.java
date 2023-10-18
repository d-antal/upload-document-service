package com.bank.exercise.pdfupload.exception;

public class RemoteServiceNotFoundException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteServiceNotFoundException(String message) {
        super(message);
    }

    public RemoteServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "RemoteServiceNotFoundException: " + getMessage();
    }

}