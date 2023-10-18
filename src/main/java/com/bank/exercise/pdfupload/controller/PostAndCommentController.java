package com.bank.exercise.pdfupload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bank.exercise.pdfupload.exception.PostAlreadyCreatedException;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.CommentEntity;
import com.bank.exercise.pdfupload.model.Post;
import com.bank.exercise.pdfupload.model.PostEntity;
import com.bank.exercise.pdfupload.service.PostAndCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping({ "bank/post-and-comment-api" })
public class PostAndCommentController {

	private final PostAndCommentService postAndCommentService;

	@PostMapping("/create/post/document-id/{documentId}")
	@ResponseStatus(HttpStatus.CREATED)
	public PostEntity createPost(@Valid @RequestBody Post post, @PathVariable(value = "documentId") Long documentId) throws PostAlreadyCreatedException {
		return postAndCommentService.createPost(post, documentId);

	}

	@GetMapping(path = "/post/document-id/{documentId}")
	public PostEntity getPostByDocumentId(@PathVariable(value = "documentId") Long documentId) throws ResourceNotFoundException {
		return postAndCommentService.findPostByDocumentId(documentId);
	}

	@PostMapping("/create/comment/post-id/{postId}")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentEntity createComment(@Valid @RequestBody Comment comment, @PathVariable(value = "postId") Long postId) {
		return postAndCommentService.createComment(comment, postId);
	}
}
