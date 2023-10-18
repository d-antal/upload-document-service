package com.bank.exercise.pdfupload.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Lob
	@NotNull()
	@Column(name = "content", nullable = false)
	private byte[] content;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "user_id", nullable = false)
	@NotNull()
	private Long userId;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "document")
	private PostEntity post;

	public Document(@NotNull byte[] content, String name, String type, @NotNull Long userId) {
		super();
		this.content = content;
		this.name = name;
		this.type = type;
		this.userId = userId;
	}
}
