package com.bank.exercise.pdfupload.service;

import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import com.bank.exercise.pdfupload.exception.PostAlreadyCreatedException;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.CommentEntity;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.model.DocumentUploadConstants;
import com.bank.exercise.pdfupload.model.Post;
import com.bank.exercise.pdfupload.model.PostEntity;
import com.bank.exercise.pdfupload.repository.CommentRepository;
import com.bank.exercise.pdfupload.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Import(FeignClientsConfiguration.class)
@Transactional
public class PostAndCommentServiceImpl implements PostAndCommentService {

	private final DocumentService documentService;
	private final PostRepository postRepository;
	private final PostAndCommentClient postAndCommentClient;
	private final CommentRepository commentRepository;

	private static final String POST_NOT_FOUND_MESSAGE = "Post not found by id: ";
	private static final String POST_NOT_FOUND_BY_DOCUMENT_ID_MESSAGE = "Post not found by document id: ";

	public PostEntity createPost(Post post, Long documentId) throws PostAlreadyCreatedException, ResourceNotFoundException {
		log.info(DocumentUploadConstants.CREATE_RESOURCE);

		Post consumedPost = postAndCommentClient.createPost(post);

		Document document = documentService.findDocumentById(documentId);

		if (postRepository.findByDocumentId(documentId).isPresent()) {
			throw new PostAlreadyCreatedException("Post already created for document: " + documentId);
		}
		PostEntity postEntity = new PostEntity(post.getUserId(), consumedPost.getTitle(), consumedPost.getBody(), document);

		postRepository.save(postEntity);

		return postEntity;
	}

	@Override
	public PostEntity findpostById(Long postId) throws ResourceNotFoundException {
		log.info("Call findpostById, id: " + postId);
		return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND_MESSAGE + postId));
	}

	@Override
	public PostEntity findPostByDocumentId(Long documentId) throws ResourceNotFoundException {
		return postRepository.findByDocumentId(documentId).orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND_BY_DOCUMENT_ID_MESSAGE + documentId));
	}

	@Override
	public CommentEntity createComment(Comment comment, Long postId) throws ResourceNotFoundException {
		log.info(DocumentUploadConstants.CREATE_RESOURCE);
		Comment consumedComment = postAndCommentClient.createComment(comment);
		PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND_MESSAGE + postId));
		CommentEntity commentEntity = new CommentEntity(consumedComment.getBody(), consumedComment.getName(), consumedComment.getEmail(), postEntity);
		commentRepository.save(commentEntity);
		return commentEntity;
	}

}
