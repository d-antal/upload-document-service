package com.bank.exercise.pdfupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.Generated;

@Generated
@SpringBootApplication
@EnableFeignClients
public class PdfUploadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfUploadServiceApplication.class, args);
	}

}
