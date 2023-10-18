package com.bank.exercise.pdfupload.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.exercise.pdfupload.model.Document;



@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	Optional<List<Document>> findAllByUserId (long userId);

}
