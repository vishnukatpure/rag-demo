package com.example.rag_pipeline.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rag_pipeline.model.RagData;
import com.example.rag_pipeline.model.User;

public interface RagDataRepository extends JpaRepository<RagData, Long> {

	Page<RagData> findAll(Pageable pageable);

	Page<RagData> findByUser(User user, Pageable pageable);
}
