package com.example.rag_pipeline.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.rag_pipeline.dto.RagDataDTO;
import com.example.rag_pipeline.dto.TextSaveRequest;
import com.example.rag_pipeline.model.RagData;
import com.example.rag_pipeline.model.User;
import com.example.rag_pipeline.services.RagDataService;
import com.example.rag_pipeline.services.UserService;
import com.example.rag_pipeline.services.generic.GenericService;
import com.example.rag_pipeline.services.rag.RagDocumentService;

@RestController
@RequestMapping("/documents")
public class RagDocumentController extends GenericService {

	private RagDocumentService ragDocumentService;

	private RagDataService ragDataService;

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "txt", "docx", "md");

	public RagDocumentController(RagDocumentService ragDocumentService, RagDataService ragDataService,
			UserService userService) {
		super(userService);
		this.ragDocumentService = ragDocumentService;
		this.ragDataService = ragDataService;
	}

	@GetMapping(value = "/data")
	public ResponseEntity<?> getData(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		if (page < 0) {
			page = 0;
		}
		if (size < 1) {
			size = 10;
		}
		if (size > 50) {
			size = 50;
		}
		Pageable pageable = PageRequest.of(page, size);
		Page<RagData> pageData = ragDataService.getAll(pageable);
		Page<RagDataDTO> dtoData = pageData.map(person -> getMapper().map(person, RagDataDTO.class));

		return ResponseEntity.ok(dtoData);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Please select a file."));
		}

		String filename = file.getOriginalFilename();

		if (filename == null || !filename.contains(".")) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid filename."));
		}

		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(java.util.Locale.ROOT);

		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			return ResponseEntity.badRequest().body(Map.of("error", "Supported formats: PDF, TXT, DOCX, MD."));
		}
		User user = getLoggedInUser();
		if (user == null) {
			return ResponseEntity.status(401).body(Map.of("error", "User is not authenticated."));
		}

		RagData ragData = ragDataService.saveFile(file);
		ResponseEntity<?> result = ragDocumentService.uploadDocument(file, filename, user.getId(), ragData.getId());
		return result;
	}

	@PostMapping(value = "/add-text")
	public ResponseEntity<?> addText(@RequestBody TextSaveRequest request) throws IOException {
		if (request == null || request.info() == null || request.info().isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Please add text."));
		}

		User user = getLoggedInUser();
		if (user == null) {
			return ResponseEntity.status(401).body(Map.of("error", "User is not authenticated."));
		}

		RagData ragData = ragDataService.saveText(request.info());
		ResponseEntity<?> result = ragDocumentService.addText(request.info(), user.getId(), ragData.getId());
		return result;
	}

	@DeleteMapping(value = "/data-to-delete")
	public ResponseEntity<?> delete(@RequestParam("dataToDelete") Long id) throws IOException {
		User user = getLoggedInUser();
		if (user == null) {
			return ResponseEntity.status(401).body(Map.of("error", "User is not authenticated."));
		}
		if (!ragDataService.existsById(id)) {
			return ResponseEntity.status(404).body(Map.of("error", "Document not found."));
		}
		ragDataService.delete(id);
		return ragDocumentService.delete(id, user.getId());
	}

}
