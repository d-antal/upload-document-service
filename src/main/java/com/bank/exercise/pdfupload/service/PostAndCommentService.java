package com.bank.exercise.pdfupload.service;

import com.bank.exercise.pdfupload.exception.PostAlreadyCreatedException;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.CommentEntity;
import com.bank.exercise.pdfupload.model.Post;
import com.bank.exercise.pdfupload.model.PostEntity;

public interface PostAndCommentService {

	PostEntity createPost(Post post, Long documentId) throws PostAlreadyCreatedException;

	PostEntity findpostById(Long postId) throws ResourceNotFoundException;

	PostEntity findPostByDocumentId(Long documentId) throws ResourceNotFoundException;
	
	CommentEntity createComment(Comment comment, Long postId);

}
