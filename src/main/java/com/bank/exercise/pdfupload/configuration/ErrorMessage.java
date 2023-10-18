package com.bank.exercise.pdfupload.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String path;

}