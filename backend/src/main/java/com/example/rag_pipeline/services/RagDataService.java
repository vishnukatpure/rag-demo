package com.example.rag_pipeline.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rag_pipeline.model.FileEntity;
import com.example.rag_pipeline.model.RagData;
import com.example.rag_pipeline.model.TextEntity;
import com.example.rag_pipeline.repository.RagDataRepository;
import com.example.rag_pipeline.repository.TextEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class RagDataService {

	private RagDataRepository ragDataRepository;
	private FileEntityService fileEntityService;
	private TextEntityRepository textEntityRepository;

	RagDataService(RagDataRepository ragDataRepository, FileEntityService fileEntityService,
			TextEntityRepository textEntityRepository) {
		this.ragDataRepository = ragDataRepository;
		this.fileEntityService = fileEntityService;
		this.textEntityRepository = textEntityRepository;
	}

	public RagData saveFile(MultipartFile file) throws IOException {
		FileEntity fileEntity = fileEntityService.uploadFile(file);
		RagData data = new RagData();
		data.setFileEntity(fileEntity);
		return ragDataRepository.save(data);
	}

	public RagData saveText(String info) {
		TextEntity entity = new TextEntity();
		entity.setTextData(info);
		entity = textEntityRepository.save(entity);

		RagData data = new RagData();
		data.setTextEntity(entity);
		return ragDataRepository.save(data);
	}

	public boolean existsById(Long id) {
		return ragDataRepository.existsById(id);
	}

	@Transactional
	public boolean delete(Long id) {
		Optional<RagData> data = ragDataRepository.findById(id);
		if (data.isEmpty()) {
			return false;
		}

		ragDataRepository.delete(data.get());
		return true;
	}

	public Page<RagData> getAll(Pageable pageable) {
		return ragDataRepository.findAll(pageable);
	}
}
