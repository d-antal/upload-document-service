package com.bank.exercise.pdfupload;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.bank.exercise.pdfupload.model.Document;

public final class DocumentTestConstants {

	static final long USER_ID_1 = 1l;
	static final long ID_NOT_EXIST = 123l;
	static final Document DOCUMENT_1 = new Document(new byte[0], "name", "type", 123l);
	static final MockMultipartFile FILE_VALID = new MockMultipartFile("file", "test.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "bytes".getBytes());
	static final String DOCUMENTS_CONTROLLER_URI = "/bank/documents/";
	static final String Document_NOT_FOUND_ERROR_MESSAGE = "Document not found by id: ";
	static final String EXPECTED_EXCEPTION_NOT_FOUND = "DocumentNotFoundException was expected";
	static final List<Document> Document_LIST = Arrays.asList(DOCUMENT_1);

}
