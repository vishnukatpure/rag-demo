package com.example.rag_pipeline.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rag_data")
public class RagData extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -608407794416805147L;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private FileEntity fileEntity;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private TextEntity textEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public FileEntity getFileEntity() {
		return fileEntity;
	}

	public void setFileEntity(FileEntity fileEntity) {
		this.fileEntity = fileEntity;
	}

	public TextEntity getTextEntity() {
		return textEntity;
	}

	public void setTextEntity(TextEntity textEntity) {
		this.textEntity = textEntity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
