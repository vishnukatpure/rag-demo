package com.example.rag_pipeline.doc_chunking;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

@Service
public class FixedLengthStrategy implements ChunkingStrategy {

	private final TokenTextSplitter textSplitter = TokenTextSplitter.builder().build();

	@Override
	public List<Document> apply(List<Document> documents) {
		return textSplitter.apply(documents);
	}

}
