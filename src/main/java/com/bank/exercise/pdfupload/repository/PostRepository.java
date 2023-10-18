package com.bank.exercise.pdfupload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.exercise.pdfupload.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

	Optional<PostEntity> findByDocumentId(Long documentId);
}
