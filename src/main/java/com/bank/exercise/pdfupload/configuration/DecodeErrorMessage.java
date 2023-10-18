package com.bank.exercise.pdfupload.configuration;

import java.io.IOException;
import java.io.InputStream;

import com.bank.exercise.pdfupload.exception.RemoteServiceBadRequestException;
import com.bank.exercise.pdfupload.exception.RemoteServiceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class DecodeErrorMessage implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorMessage errorMessage;
        try (InputStream bodyIs = response.body()
            .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            errorMessage = mapper.readValue(bodyIs, ErrorMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        switch (response.status()) {
        case 400:
            return new RemoteServiceBadRequestException(errorMessage.getMessage() != null ? errorMessage.getMessage() : "RemoteServiceBadRequestException");
        case 404:
            return new RemoteServiceNotFoundException(errorMessage.getMessage() != null ? errorMessage.getMessage() : "RemoteServiceNotFoundException");
        default:
            return errorDecoder.decode(methodKey, response);
        }
    }
}