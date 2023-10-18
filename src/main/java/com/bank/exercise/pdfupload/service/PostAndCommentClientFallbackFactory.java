package com.bank.exercise.pdfupload.service;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.bank.exercise.pdfupload.exception.RemoteServiceBadRequestException;
import com.bank.exercise.pdfupload.exception.RemoteServiceNotFoundException;
import com.bank.exercise.pdfupload.exception.UploadFailedException;
import com.bank.exercise.pdfupload.exception.WrongRemoteServerAddressException;
import com.bank.exercise.pdfupload.exception.WrongRemoteServerRequestException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.Post;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PostAndCommentClientFallbackFactory implements FallbackFactory<PostAndCommentClient> {

	@Override
	public PostAndCommentClient create(Throwable cause) {
		return new PostAndCommentClient() {

			@Override
			public Post createPost(Post post) {
				handleException(cause);
				return post;
			}

			@Override
			public Comment createComment(Comment comment) {
				handleException(cause);
				return comment;
			}

			private void handleException(Throwable cause) {
				if (cause instanceof RemoteServiceBadRequestException) {
					throw new WrongRemoteServerRequestException(cause.getMessage());
				}
				if (cause instanceof RemoteServiceNotFoundException) {
					throw new WrongRemoteServerAddressException(cause.getMessage());
				}
				if (cause instanceof Exception) {
					throw new UploadFailedException(cause.getMessage());
				}
				log.info("PostAndCommentClient fallback service call was successfull");
			}

		};
	}
}