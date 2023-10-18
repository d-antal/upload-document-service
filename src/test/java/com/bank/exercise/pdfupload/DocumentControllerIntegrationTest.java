package com.bank.exercise.pdfupload;

import static com.bank.exercise.pdfupload.DocumentTestConstants.DOCUMENTS_CONTROLLER_URI;
import static com.bank.exercise.pdfupload.DocumentTestConstants.DOCUMENT_1;
import static com.bank.exercise.pdfupload.DocumentTestConstants.FILE_VALID;
import static com.bank.exercise.pdfupload.DocumentTestConstants.ID_NOT_EXIST;
import static com.bank.exercise.pdfupload.DocumentTestConstants.USER_ID_1;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bank.exercise.pdfupload.controller.DocumentController;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.service.DocumentService;

@WebMvcTest(DocumentController.class)
class DocumentControllerIntegrationTest {

	private static final String UPLOAD_URI = "/upload";
	private static final String REMOVE_URI = "/remove/";
	private static final String DOCUMENT_URI = "/document";
	private static final String USER_URI = "user/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DocumentService documentService;
	

	private static Stream<MockMultipartFile> validFiles() {
		return Stream.of(FILE_VALID);
	}

	@Test
	void testGetAllDocumentById() throws Exception {
		List<Document> documents = Arrays.asList(DOCUMENT_1);
		when(documentService.findAllByUserId(USER_ID_1)).thenReturn(documents);
		mockMvc.perform(get(DOCUMENTS_CONTROLLER_URI + USER_URI + USER_ID_1 + DOCUMENT_URI).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(1)));
		verify(documentService, times(1)).findAllByUserId(USER_ID_1);
	}

	@Test
	void testGetAllDocumentByIdWhenUserNotFound() throws Exception {
		when(documentService.findAllByUserId(ID_NOT_EXIST)).thenThrow(ResourceNotFoundException.class);
		mockMvc.perform(get(DOCUMENTS_CONTROLLER_URI + USER_URI + ID_NOT_EXIST + DOCUMENT_URI).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(documentService, times(1)).findAllByUserId(ID_NOT_EXIST);
	}

	@ParameterizedTest
	@MethodSource("validFiles")
	void testUploadDocumentWhenRequestBodyIsValid(MultipartFile document) throws Exception {
		Document pdfDocument = new Document(document.getBytes(), StringUtils.cleanPath(document.getOriginalFilename()), document.getContentType(), USER_ID_1);
		when(documentService.uploadDocument(document, USER_ID_1)).thenReturn(pdfDocument);

		mockMvc.perform(multipart(DOCUMENTS_CONTROLLER_URI + USER_URI + USER_ID_1 + UPLOAD_URI).file((MockMultipartFile) document))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.content").isNotEmpty())
		.andExpect(jsonPath("$.name").value(pdfDocument.getName())).andExpect(jsonPath("$.type").value(document.getContentType()))
		.andExpect(jsonPath("$.userId").value(String.valueOf(USER_ID_1)));

		verify(documentService, times(1)).uploadDocument(document, USER_ID_1);
	}

	@Test
	void testRemoveDocument() throws Exception {
		when(documentService.removeDocument(USER_ID_1)).thenReturn(USER_ID_1);
		mockMvc.perform(delete(DOCUMENTS_CONTROLLER_URI + REMOVE_URI + USER_ID_1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		verify(documentService, times(1)).removeDocument(USER_ID_1);
	}

	@Test
	void testRemoveDocumentWhenDocumentNotFound() throws Exception {
		when(documentService.removeDocument(ID_NOT_EXIST)).thenThrow(ResourceNotFoundException.class);
		mockMvc.perform(delete(DOCUMENTS_CONTROLLER_URI + REMOVE_URI + ID_NOT_EXIST).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
		verify(documentService, times(1)).removeDocument(ID_NOT_EXIST);
	}

}
