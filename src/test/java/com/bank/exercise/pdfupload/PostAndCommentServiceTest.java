package com.bank.exercise.pdfupload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bank.exercise.pdfupload.exception.PostAlreadyCreatedException;
import com.bank.exercise.pdfupload.exception.ResourceNotFoundException;
import com.bank.exercise.pdfupload.model.Comment;
import com.bank.exercise.pdfupload.model.CommentEntity;
import com.bank.exercise.pdfupload.model.Document;
import com.bank.exercise.pdfupload.model.Post;
import com.bank.exercise.pdfupload.model.PostEntity;
import com.bank.exercise.pdfupload.repository.CommentRepository;
import com.bank.exercise.pdfupload.repository.PostRepository;
import com.bank.exercise.pdfupload.service.DocumentService;
import com.bank.exercise.pdfupload.service.PostAndCommentClient;
import com.bank.exercise.pdfupload.service.PostAndCommentService;
import com.bank.exercise.pdfupload.service.PostAndCommentServiceImpl;

class PostAndCommentServiceTest {

	private static final Long ID_1 = 1l;
	private static final Long POST_ID_NOT_EXIST = 123l;
	private static final Long DOUMENT_ID_1 = 1l;
	private static final Long DOCUMENT_ID_NOT_EXISTS = 123l;
	private static final Document DOCUMENT_1 = new Document(new byte[0], "name", "type", 1l);
	private static final Post POST = Post.builder().id(ID_1).body("test body").userId(ID_1).title("test title").build();
	private final static Comment COMMENT = Comment.builder().id(ID_1).body("test post body " + ID_1).postId(ID_1).email("test email " + ID_1).name("test name " + ID_1).build();
	private static final PostEntity POST_ENTITY = new PostEntity(POST.getUserId(), POST.getTitle(), POST.getBody(), DOCUMENT_1);

	private PostAndCommentService postAndCommentService;
	@Mock
	private PostRepository postRepository;
	@Mock
	private DocumentService documentService;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private PostAndCommentClient postAndCommentClient;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		postAndCommentService = new PostAndCommentServiceImpl(documentService, postRepository, postAndCommentClient, commentRepository);
	}

	@Test
	void testCreatePost() throws PostAlreadyCreatedException {
		when(postAndCommentClient.createPost(POST)).thenReturn(POST);
		when(documentService.findDocumentById(DOUMENT_ID_1)).thenReturn(DOCUMENT_1);
		when(postRepository.findByDocumentId(DOUMENT_ID_1)).thenReturn(Optional.empty());
		PostEntity postEntity = new PostEntity(POST.getUserId(), POST.getTitle(), POST.getBody(), DOCUMENT_1);
		when(postRepository.save(postEntity)).thenReturn(postEntity);

		PostEntity createdPost = postAndCommentService.createPost(POST, DOUMENT_ID_1);
		assertEquals(postEntity, createdPost);
		verify(postAndCommentClient, times(1)).createPost(POST);
		verify(documentService, times(1)).findDocumentById(DOUMENT_ID_1);
		verify(postRepository, times(1)).findByDocumentId(DOUMENT_ID_1);
		verify(postRepository, times(1)).save(postEntity);
	}

	@Test
	void testCreatePostWhenDocumentNotExists() throws PostAlreadyCreatedException {
		when(postAndCommentClient.createPost(POST)).thenReturn(POST);
		when(documentService.findDocumentById(DOCUMENT_ID_NOT_EXISTS)).thenThrow(new ResourceNotFoundException("Document Not Found by id"));

		assertThrows(ResourceNotFoundException.class, () -> postAndCommentService.createPost(POST, DOCUMENT_ID_NOT_EXISTS));

		verify(postRepository, never()).findByDocumentId(DOCUMENT_ID_NOT_EXISTS);
		verify(postRepository, never()).save(any(PostEntity.class));
	}

	@Test
	void testCreatePostWhenPostAlreadyExists() throws PostAlreadyCreatedException {
		when(postAndCommentClient.createPost(POST)).thenReturn(POST);
		when(documentService.findDocumentById(DOUMENT_ID_1)).thenReturn(DOCUMENT_1);
		when(postRepository.findByDocumentId(DOUMENT_ID_1)).thenReturn(Optional.of(POST_ENTITY));

		assertThrows(PostAlreadyCreatedException.class, () -> postAndCommentService.createPost(POST, DOUMENT_ID_1));

		verify(postRepository, never()).save(any(PostEntity.class));
	}

	@Test
	void testFindpostById() {
		when(postRepository.findById(ID_1)).thenReturn(Optional.of(POST_ENTITY));
		PostEntity postEntity = postAndCommentService.findpostById(ID_1);
		assertEquals(POST_ENTITY, postEntity);
		verify(postRepository, times(1)).findById(ID_1);
	}

	@Test
	void testFindPostByIdWhenPostNotFound() {
		when(postRepository.findById(POST_ID_NOT_EXIST)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> postAndCommentService.findpostById(POST_ID_NOT_EXIST));
		verify(postRepository, times(1)).findById(POST_ID_NOT_EXIST);
	}

	@Test
	void testFindPostByIdWhenIdIsNull() {
		when(postRepository.findById(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> postAndCommentService.findpostById(null));
		verify(postRepository, times(1)).findById(null);
	}

	@Test
	void testFindPostByDocumentId() {
		when(postRepository.findByDocumentId(DOUMENT_ID_1)).thenReturn(Optional.of(POST_ENTITY));
		PostEntity postEntity = postAndCommentService.findPostByDocumentId(DOUMENT_ID_1);
		assertEquals(POST_ENTITY, postEntity);
		verify(postRepository, times(1)).findByDocumentId(ID_1);
	}

	@Test
	void testFindPostByDocumentIdWhenPostNotFound() {
		when(postRepository.findByDocumentId(DOUMENT_ID_1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> postAndCommentService.findPostByDocumentId(DOUMENT_ID_1));
		verify(postRepository, times(1)).findByDocumentId(DOUMENT_ID_1);
	}

	@Test
	void findPostByDocumentIdWhenIdIsNull() {
		when(postRepository.findByDocumentId(null)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> postAndCommentService.findPostByDocumentId(null));
		verify(postRepository, times(1)).findByDocumentId(null);
	}

	@Test
	void testCreateComment() throws ResourceNotFoundException {
		when(postAndCommentClient.createComment(COMMENT)).thenReturn(COMMENT);
		when(postRepository.findById(ID_1)).thenReturn(Optional.of(POST_ENTITY));

		CommentEntity commentEntity = new CommentEntity(COMMENT.getBody(), COMMENT.getName(), COMMENT.getEmail(), POST_ENTITY);
		when(commentRepository.save(commentEntity)).thenReturn(commentEntity);

		CommentEntity createdCommentEntity = postAndCommentService.createComment(COMMENT, ID_1);
		assertEquals(commentEntity, createdCommentEntity);
		verify(postAndCommentClient, times(1)).createComment(COMMENT);
		verify(postRepository, times(1)).findById(ID_1);
		verify(commentRepository, times(1)).save(commentEntity);

	}

	@Test
	void testCommentWhenPostNotExists() throws PostAlreadyCreatedException {
		when(postAndCommentClient.createComment(COMMENT)).thenReturn(COMMENT);
		when(postRepository.findById(POST_ID_NOT_EXIST)).thenThrow(new ResourceNotFoundException("Post Not Found"));

		assertThrows(ResourceNotFoundException.class, () -> postAndCommentService.createComment(COMMENT, POST_ID_NOT_EXIST));

		verify(commentRepository, never()).save(any(CommentEntity.class));
	}
}
