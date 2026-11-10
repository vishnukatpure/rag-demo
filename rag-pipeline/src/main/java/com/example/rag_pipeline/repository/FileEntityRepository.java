package com.example.rag_pipeline.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rag_pipeline.model.FileEntity;

public interface FileEntityRepository extends CrudRepository<FileEntity, Long> {

	Optional<FileEntity> findByFileId(String key);

	@Modifying
	@Transactional
	long deleteByFileId(String uuid);

}
