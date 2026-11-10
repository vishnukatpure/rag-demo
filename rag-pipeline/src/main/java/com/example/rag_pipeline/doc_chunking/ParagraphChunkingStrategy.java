package com.example.rag_pipeline.doc_chunking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class ParagraphChunkingStrategy implements ChunkingStrategy {

	@Override
	public List<Document> apply(List<Document> documents) {
		List<Document> chunks = new ArrayList<>();

		for (Document document : documents) {
			String[] paragraphs = document.getText().split("\\R\\s*\\R");

			for (String paragraph : paragraphs) {
				String text = paragraph.trim();

				if (!text.isBlank()) {
					chunks.add(new Document(text, document.getMetadata()));
				}
			}
		}

		return chunks;
	}

}
