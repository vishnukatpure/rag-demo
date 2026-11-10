package com.example.rag_pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rag_pipeline.model.TextEntity;

public interface TextEntityRepository extends JpaRepository<TextEntity, Long> {

}
