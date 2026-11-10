package com.example.rag_pipeline.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "text_data")
public class TextEntity extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8588507514836296558L;

	@Lob
	@Column(name = "text_data", columnDefinition = "LONGTEXT")
	private String textData;

	public String getTextData() {
		return textData;
	}

	public void setTextData(String textData) {
		this.textData = textData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
