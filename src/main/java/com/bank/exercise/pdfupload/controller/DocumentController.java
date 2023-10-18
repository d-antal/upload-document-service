package com.bank.exercise.pdfupload.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/bank/documents")
public class DocumentController {

	private static final String NOT_FOUND = "Document not found";
	private static final String INVALID_ID = "Invalid id was provided";
	private static final String SERVER_ERROR = "The Document Management Service failed to process the request";
	private final DocumentService documentService;

	@Operation(summary = "Get all Documents by userId")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of Documents by userId ", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Document.class))) }),
							@ApiResponse(responseCode = "400", description = INVALID_ID, content = @Content), 
							@ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content) })
	@GetMapping("/user/{id}/document")
	public List<Document> getAllDocumentById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
		return documentService.findAllByUserId(userId);
	}

	@Operation(summary = "Upload a new Document")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Document is saved", content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Document.class)) }),
							@ApiResponse(responseCode = "400", description = "Invalid body", content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content)})
	@PostMapping("/user/{id}/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public Document uploadDocument(@Valid @RequestParam("file") MultipartFile file, @Valid @PathVariable(value = "id") Long userId) {
		return documentService.uploadDocument(file, userId);
	}

	@Operation(summary = "Remove a Document")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Document is removed", content = { @Content(mediaType = APPLICATION_JSON_VALUE) }),
							@ApiResponse(responseCode = "400", description = INVALID_ID, content = @Content), 
							@ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content),
							@ApiResponse(responseCode = "500", description = SERVER_ERROR, content = @Content)})
	@DeleteMapping("/remove/{id}")
	public void removeDocument(@PathVariable(value = "id") Long documentId) throws ResourceNotFoundException {
		documentService.removeDocument(documentId);
	}
}
