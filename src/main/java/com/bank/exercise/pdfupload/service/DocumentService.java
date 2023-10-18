package com.bank.exercise.pdfupload.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Document;

public interface DocumentService {

	Document uploadDocument(MultipartFile file, Long userId);

	Document findDocumentById(Long documentId);

	long removeDocument(Long documentId) throws ResourceNotFoundException;

	List<Document> findAllByUserId(long userId) throws ResourceNotFoundException;

}
