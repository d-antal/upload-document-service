package com.bank.exercise.pdfupload.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.bank.exercise.pdfupload.configuration.FallbackSupportConfig;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.Post;

@FeignClient(value = "postAndComentApi", url = "https://jsonplaceholder.typicode.com/", fallbackFactory = PostAndCommentClientFallbackFactory.class, configuration = FallbackSupportConfig.class)
public interface PostAndCommentClient {

	@PostMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	Post createPost(Post post);

	@PostMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	Comment createComment(Comment comment);
}