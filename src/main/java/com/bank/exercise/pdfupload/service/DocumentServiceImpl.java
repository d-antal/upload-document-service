package com.bank.exercise.pdfupload.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.exception.UploadFailedException;
import com.bank.exercise.pdfupload.exception.WrongExtensionException;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.repository.DocumentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {

	private static final String DOCUMENT_NOT_FOUND_MESSAGE = "Document not found by id: ";
	private static final String WRONG_FILE_EXTENSION_ERROR_MESSAGE = "Wrong file extension:  ";
	private final DocumentRepository documentRepository;

	@Override
	public List<Document> findAllByUserId(long userId) throws ResourceNotFoundException {
		log.info("Call findAllByUserId : " + userId);
		return documentRepository.findAllByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_NOT_FOUND_MESSAGE + userId));
	}

	@Override
	public Document findDocumentById(Long documentId) throws ResourceNotFoundException {
		log.info("Call findDocumentById, id: " + documentId);
		return documentRepository.findById(documentId).orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_NOT_FOUND_MESSAGE + documentId));
	}

	@Override
	public Document uploadDocument(MultipartFile file, Long userId) {
		Document pdfDocument;
		try {
			if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("pdf")) {
				throw new WrongExtensionException(WRONG_FILE_EXTENSION_ERROR_MESSAGE + FilenameUtils.getExtension(file.getOriginalFilename()));
			}
			pdfDocument = new Document(file.getBytes(), file.getOriginalFilename(), file.getContentType(), userId);
		} catch (IOException e) {
			throw new UploadFailedException(e.getMessage());
		}
		return documentRepository.save(pdfDocument);
	}

	@Override
	public long removeDocument(Long documentId) throws ResourceNotFoundException {
		Document document = documentRepository.findById(documentId).orElseThrow(() -> new ResourceNotFoundException(DOCUMENT_NOT_FOUND_MESSAGE + documentId));
		documentRepository.delete(document);
		return document.getId();
	}

}
