package com.example.rag_pipeline.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class FileEntity extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8588507514836296558L;

	@Column(name = "file_id", nullable = false, unique = true)
	private String fileId;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_size")
	private long fileSize;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "file_data", columnDefinition = "LONGBLOB")
	private byte[] fileData;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
