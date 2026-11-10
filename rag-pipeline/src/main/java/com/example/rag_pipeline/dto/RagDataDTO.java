package com.example.rag_pipeline.dto;

public class RagDataDTO extends AbstractEntityBaseDTO {

	private FileEntityDTO fileEntity;

	private TextEntityDTO textEntity;

	public FileEntityDTO getFileEntity() {
		return fileEntity;
	}

	public void setFileEntity(FileEntityDTO fileEntity) {
		this.fileEntity = fileEntity;
	}

	public TextEntityDTO getTextEntity() {
		return textEntity;
	}

	public void setTextEntity(TextEntityDTO textEntity) {
		this.textEntity = textEntity;
	}

}
