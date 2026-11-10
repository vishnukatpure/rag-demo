package com.example.rag_pipeline.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag_pipeline.dto.FileDownload;
import com.example.rag_pipeline.services.FileEntityService;

import jakarta.validation.constraints.NotBlank;

@Validated
@RequestMapping("/file")
@RestController
public class FileResource {

	FileEntityService fileEntityService;

	FileResource(FileEntityService fileEntityService) {
		this.fileEntityService = fileEntityService;
	}

	@GetMapping(value = "/{key}")
	public ResponseEntity<Resource> getFileByKey(@PathVariable @NotBlank(message = "Key is Empty") String key) {

		FileDownload file = fileEntityService.downloadFile(key);

		Resource resource = new InputStreamResource(file.inputStream());

		String contentType = file.contentType() == null || file.contentType().isBlank()
				? MediaType.APPLICATION_OCTET_STREAM_VALUE
				: file.contentType();
		String fileName = file.fileName() == null ? "download" : file.fileName().replace("\"", "");

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.contentLength(file.contentLength())
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
}
