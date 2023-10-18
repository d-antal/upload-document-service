package com.bank.exercise.pdfupload;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bank.exercise.pdfupload.controller.PostAndCommentController;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.CommentEntity;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.model.Post;
import com.bank.exercise.pdfupload.model.PostEntity;
import com.bank.exercise.pdfupload.service.PostAndCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PostAndCommentController.class)
class PostAndCommentControllerIntegrationTest {

	private static final String FIND_POST_URI = "/post/document-id/";
	private static final String CREATE_POST_URI = "/create/post/document-id/";
	private static final String CREATE_COMMENT_URI = "/create/comment/post-id/";
	private static final String BASE_URI = "/bank/post-and-comment-api";
	private static final Long ID_1 = 1L;
	private static final Long POST_ID_NOT_EXISTS = 123l;
	private static final Long DOUMENT_ID_1 = 1l;
	private static final Long DOCUMENT_ID_NOT_EXISTS = 123l;
	private static final Document DOCUMENT_1 = new Document(new byte[0], "name", "type", 1l);
	private static final Post POST = Post.builder().id(ID_1).body("test body").userId(ID_1).title("test title").build();
	private static final Comment COMMENT = Comment.builder().id(ID_1).body("test post body " + ID_1).postId(ID_1).email("test email " + ID_1).name("test name " + ID_1).build();
	private static final PostEntity POST_ENTITY = new PostEntity(POST.getId(), POST.getUserId(), POST.getTitle(), POST.getBody(), DOCUMENT_1);
	private static final CommentEntity COMMENT_ENTITY = new CommentEntity(COMMENT.getId(), COMMENT.getBody(), COMMENT.getName(), COMMENT.getEmail(), POST_ENTITY);
	private static final ObjectMapper OM = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PostAndCommentService postAndCommentService;

	@Test
	void testCreatePost() throws Exception {
		when(postAndCommentService.createPost(POST, DOUMENT_ID_1)).thenReturn(POST_ENTITY);

		mockMvc.perform(post(BASE_URI + CREATE_POST_URI + DOUMENT_ID_1).content(OM.writeValueAsString(POST)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(String.valueOf(POST.getId())))
				.andExpect(jsonPath("$.userId").value(String.valueOf(POST.getUserId())))
				.andExpect(jsonPath("$.body", is(POST.getBody())))
				.andExpect(jsonPath("$.document").isNotEmpty());

		verify(postAndCommentService, times(1)).createPost(POST, DOUMENT_ID_1);
	}

	@Test
	void testCreatePostWhenDocumentNotFound() throws Exception {
		when(postAndCommentService.createPost(POST, DOCUMENT_ID_NOT_EXISTS)).thenThrow(ResourceNotFoundException.class);
	
		mockMvc.perform(post(BASE_URI + CREATE_POST_URI + DOCUMENT_ID_NOT_EXISTS).content(OM.writeValueAsString(POST)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(postAndCommentService, times(1)).createPost(POST, DOCUMENT_ID_NOT_EXISTS);
	}

	@Test
	 void getPostByDocumentId() throws Exception {
		when(postAndCommentService.findPostByDocumentId(DOUMENT_ID_1)).thenReturn(POST_ENTITY);

		mockMvc.perform(get(BASE_URI + FIND_POST_URI+ DOUMENT_ID_1).content(OM.writeValueAsString(POST)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(String.valueOf(POST.getId())))
		.andExpect(jsonPath("$.userId").value(String.valueOf(POST.getUserId())))
		.andExpect(jsonPath("$.body", is(POST.getBody())))
		.andExpect(jsonPath("$.document").isNotEmpty());

		verify(postAndCommentService, times(1)).findPostByDocumentId(DOUMENT_ID_1);
	}
	
	@Test
	void testGetPostByDocumentIdWhenDocumentNotExists() throws Exception {
		when(postAndCommentService.findPostByDocumentId(DOCUMENT_ID_NOT_EXISTS)).thenThrow(ResourceNotFoundException.class);
	
		mockMvc.perform(get(BASE_URI + FIND_POST_URI + POST_ID_NOT_EXISTS).content(OM.writeValueAsString(POST)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(postAndCommentService, times(1)).findPostByDocumentId( DOCUMENT_ID_NOT_EXISTS);
	}
	
	@Test
	void testCreateComment() throws Exception {
		when(postAndCommentService.createComment(COMMENT, ID_1)).thenReturn(COMMENT_ENTITY);

		mockMvc.perform(post(BASE_URI + CREATE_COMMENT_URI + ID_1).content(OM.writeValueAsString(COMMENT)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(String.valueOf(COMMENT.getId())))
				.andExpect(jsonPath("$.body", is(COMMENT.getBody())))
				.andExpect(jsonPath("$.name", is(COMMENT.getName())))
				.andExpect(jsonPath("$.email", is(COMMENT.getEmail())))
				.andExpect(jsonPath("$.post").isNotEmpty());

		verify(postAndCommentService, times(1)).createComment(COMMENT, ID_1);
	}

	@Test
	void testCreateCommmentWhenPostNotFound() throws Exception {
		when(postAndCommentService.createComment(COMMENT, POST_ID_NOT_EXISTS)).thenThrow(ResourceNotFoundException.class);
	
		mockMvc.perform(post(BASE_URI + CREATE_COMMENT_URI + POST_ID_NOT_EXISTS).content(OM.writeValueAsString(COMMENT)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(postAndCommentService, times(1)).createComment(COMMENT, POST_ID_NOT_EXISTS);
	}

}
