package com.example.rag_pipeline.doc_chunking;

import java.util.List;

import org.springframework.ai.document.Document;

public interface ChunkingStrategy {
	List<Document> apply(List<Document> documents);
}
