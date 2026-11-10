package com.example.rag_pipeline.services.rag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rag_pipeline.doc_chunking.ChunkingStrategy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;

@Service
public class RagDocumentService {

	private final VectorStore vectorStore;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final ElasticsearchClient elasticsearchClient;
	private final ChunkingStrategy chunkingStrategy;

	private final String indexName;

	private final TokenTextSplitter textSplitter = TokenTextSplitter.builder().build();

	RagDocumentService(VectorStore vectorStore, ElasticsearchClient elasticsearchClient,
			@Value("${spring.ai.vectorstore.elasticsearch.index-name}") String indexName,
			ChunkingStrategy chunkingStrategy) {
		this.vectorStore = vectorStore;
		this.elasticsearchClient = elasticsearchClient;
		this.indexName = indexName;
		this.chunkingStrategy = chunkingStrategy;
	}

	public ResponseEntity<?> uploadDocument(MultipartFile file, String filename, Long userId, Long ragDataId)
			throws IOException {

		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(java.util.Locale.ROOT);
		Path tempFile = Files.createTempFile("rag-upload-", "." + extension);

		try {
			Files.write(tempFile, file.getBytes());
			TikaDocumentReader reader = new TikaDocumentReader(new FileSystemResource(tempFile));

			List<Document> documents = reader.read();
			// 1. Add user metadata to extracted documents
			for (Document document : documents) {
				document.getMetadata().put("userId", String.valueOf(userId));
				document.getMetadata().put("id", String.valueOf(ragDataId));
				document.getMetadata().put("fileName", filename);
			}

			// 2. Split the extracted text into smaller chunks
			List<Document> chunks = chunkingStrategy.apply(documents);

			if (chunks.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("error", "No readable text found in the document."));
			}

			// 3. Generate embeddings and save chunks to Elasticsearch
			vectorStore.add(chunks);

			return ResponseEntity.ok(Map.of("message", "Document uploaded successfully", "filename", filename,
					"chunksStored", chunks.size()));
		} catch (Exception e) {
			LOGGER.error("Document upload failed", e);
			return ResponseEntity.internalServerError().body(Map.of("error", "Document upload failed"));
		} finally {
			if (tempFile != null) {
				try {
					Files.deleteIfExists(tempFile);
				} catch (IOException e) {
					LOGGER.warn("Could not delete temporary file", e);
				}
			}
		}
	}

	public ResponseEntity<?> addText(String text, Long userId, Long textId) {
		try {
			Document document = new Document(text,
					Map.of("id", String.valueOf(textId), "userId", String.valueOf(userId)));

			// Split large text into smaller chunks
			List<Document> chunks = chunkingStrategy.apply(List.of(document));

			// Generate embeddings and store chunks
			vectorStore.add(chunks);

			return ResponseEntity.ok(Map.of("message", "Text added successfully", "chunksStored", chunks.size()));
		} catch (Exception e) {
			LOGGER.error("Document upload failed", e);
			return ResponseEntity.internalServerError().body(Map.of("error", "Document upload failed"));
		}
	}

	public ResponseEntity<?> delete(Long id, long userId) {
		try {
			DeleteByQueryResponse response = elasticsearchClient
					.deleteByQuery(d -> d.index(indexName)
							.query(q -> q.bool(b -> b
									.filter(f -> f.term(t -> t.field("metadata.userId").value(String.valueOf(userId))))
									.filter(f -> f.term(t -> t.field("metadata.id").value(String.valueOf(id))))))
							.refresh(true));

			long deleted = response.deleted() == null ? 0 : response.deleted();
			return ResponseEntity.ok(Map.of("message", "Document deleted successfully", "chunksDeleted", deleted));
		} catch (Exception e) {
			LOGGER.error("Document delete failed", e);
			return ResponseEntity.internalServerError().body(Map.of("error", "Document delete failed"));
		}
	}

}
