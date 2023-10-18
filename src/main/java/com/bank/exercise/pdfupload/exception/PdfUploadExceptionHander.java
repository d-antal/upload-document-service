package com.bank.exercise.pdfupload.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bank.exercise.pdfupload.model.DocumentUploadConstants;

@ControllerAdvice
public class PdfUploadExceptionHander extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundException(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Object> restApiServerError(HttpClientErrorException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), DocumentUploadConstants.REMOTE_API_SERVER_ERROR + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(PostAlreadyCreatedException.class)
	public ResponseEntity<Object> restApiServerError(PostAlreadyCreatedException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), DocumentUploadConstants.REMOTE_API_SERVER_ERROR + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UploadFailedException.class)
	public ResponseEntity<Object> restApiServerError(UploadFailedException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), DocumentUploadConstants.REMOTE_API_SERVER_ERROR + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(WrongRemoteServerRequestException.class)
	public ResponseEntity<Object> restApiServerError(WrongRemoteServerRequestException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), DocumentUploadConstants.REMOTE_API_SERVER_ERROR + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(WrongRemoteServerAddressException.class)
	public ResponseEntity<Object> restApiServerError(WrongRemoteServerAddressException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), DocumentUploadConstants.REMOTE_API_SERVER_ERROR + ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> internalServerError(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
