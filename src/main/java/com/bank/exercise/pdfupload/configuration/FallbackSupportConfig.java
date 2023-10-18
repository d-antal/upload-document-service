package com.bank.exercise.pdfupload.configuration;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class FallbackSupportConfig {

	@Bean
	public ErrorDecoder errorDecoder() {
		return new DecodeErrorMessage();
	}
}