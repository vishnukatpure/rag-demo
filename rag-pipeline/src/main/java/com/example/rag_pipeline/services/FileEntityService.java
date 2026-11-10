package com.example.rag_pipeline.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rag_pipeline.dto.FileDownload;
import com.example.rag_pipeline.model.FileEntity;
import com.example.rag_pipeline.repository.FileEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class FileEntityService {

	private final FileEntityRepository fileRepository;

	public FileEntityService(FileEntityRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@Transactional
	public FileEntity uploadFile(MultipartFile file) throws IOException {
		String key = UUID.randomUUID().toString();

		FileEntity entity = new FileEntity();
		entity.setFileId(key);
		entity.setFileName(file.getOriginalFilename());
		entity.setContentType(file.getContentType());
		entity.setFileSize(file.getSize());
		entity.setFileData(file.getBytes());

		return fileRepository.save(entity);
	}

	@Transactional
	public FileDownload downloadFile(String key) {

		FileEntity entity = fileRepository.findByFileId(key)
				.orElseThrow(() -> new IllegalArgumentException("File not found: " + key));

		InputStream inputStream = new ByteArrayInputStream(entity.getFileData());

		return new FileDownload(inputStream, entity.getFileName(), entity.getContentType(), entity.getFileSize());
	}

	@Transactional
	public boolean deleteFile(String uuid) {
		return fileRepository.deleteByFileId(uuid) > 0;
	}

}
