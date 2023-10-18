
package com.bank.exercise.pdfupload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.exception.WrongExtensionException;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.repository.DocumentRepository;
import com.bank.exercise.pdfupload.service.DocumentService;
import com.bank.exercise.pdfupload.service.DocumentServiceImpl;

class DocumentServiceTest {

	@Mock
	private DocumentRepository documentRepository;
	private DocumentService documentService;
	private final static Document DOCUMENT_1 = new Document(new byte[0], "name", "type", 123l);
	private static final List<Document> DOCUMENT_LIST = Arrays.asList(DOCUMENT_1);
	private static final Long ID_1 = 1l;
	private static final Long ID_NOT_EXIST = 1234l;
	static final MockMultipartFile FILE_VALID = new MockMultipartFile("file", "test.pdf", MediaType.MULTIPART_FORM_DATA_VALUE, "bytes".getBytes());
	static final MockMultipartFile FILE_NOT_VALID = new MockMultipartFile("file", "test.xml", MediaType.MULTIPART_FORM_DATA_VALUE, "bytes".getBytes());

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		documentService = new DocumentServiceImpl(documentRepository);

	}

	@Test
	void testFindAllByUserId() {
		when(documentRepository.findAllByUserId(ID_1)).thenReturn(Optional.of(DOCUMENT_LIST));
		List<Document> documents = documentService.findAllByUserId(ID_1);
		assertEquals(DOCUMENT_LIST, documents);
		verify(documentRepository, times(1)).findAllByUserId(ID_1);
	}

	@Test
	void testFindAllByUserIdWhenDocumentNotFound() {
		when(documentRepository.findAllByUserId(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> documentService.findAllByUserId(ID_NOT_EXIST));
		verify(documentRepository, times(1)).findAllByUserId(ID_NOT_EXIST);
	}

	@Test
	void testFindDocumentById() {
		when(documentRepository.findById(ID_1)).thenReturn(Optional.of(DOCUMENT_1));
		Document document = documentService.findDocumentById(ID_1);
		assertEquals(DOCUMENT_1, document);
		verify(documentRepository, times(1)).findById(ID_1);
	}

	@Test
	void testFindDocumentByIdWhenDocumentNotFound() {
		when(documentRepository.findById(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> documentService.findDocumentById(ID_NOT_EXIST));
		verify(documentRepository, times(1)).findById(ID_NOT_EXIST);
	}

	@Test
	void testFindDocumentByIdWhenIdIsNull() {
		when(documentRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> documentService.findDocumentById(null));
		verify(documentRepository, times(1)).findById(null);
	}

	@Test
	void testUploadDocument() throws IOException {
		Document pdfDocument = new Document(FILE_VALID.getBytes(), StringUtils.cleanPath(FILE_VALID.getOriginalFilename()), FILE_VALID.getContentType(), ID_1);
		when(documentRepository.save(pdfDocument)).thenReturn(pdfDocument);
		Document document = documentService.uploadDocument(FILE_VALID, ID_1);
		assertEquals(pdfDocument, document);
		verify(documentRepository, times(1)).save(pdfDocument);
	}

	@Test
	void testUploadDocumentWhenExtensionIsNotPdf() throws IOException {
		assertThrows(WrongExtensionException.class, () -> documentService.uploadDocument(FILE_NOT_VALID, ID_1));
		verify(documentRepository, times(0)).save(any());
	}

	@Test
	void testRemoveDocumentWhenDocumentExist() throws ResourceNotFoundException {
		when(documentRepository.findById(ID_1)).thenReturn(Optional.of(DOCUMENT_1));
		long documentId = documentService.removeDocument(ID_1);
		assertEquals(DOCUMENT_1.getId(), documentId);
		verify(documentRepository, times(1)).findById(ID_1);
		verify(documentRepository, times(1)).delete(DOCUMENT_1);
	}

	@Test
	void testRemoveDocumentWhenDocumentNotExist() throws ResourceNotFoundException {
		when(documentRepository.findById(ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> documentService.removeDocument(ID_NOT_EXIST));
		verify(documentRepository, times(1)).findById(ID_NOT_EXIST);
		verify(documentRepository, never()).delete(any(Document.class));
	}

	@Test
	void testRemoveDocumentWhenIdIsNull() throws ResourceNotFoundException {
		when(documentRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> documentService.removeDocument(null));
		verify(documentRepository, times(1)).findById(null);
		verify(documentRepository, never()).delete(any(Document.class));
	}
}
