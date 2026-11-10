package com.example.rag_pipeline.dto;

import java.io.InputStream;

public record FileDownload(InputStream inputStream, String fileName, String contentType, long contentLength) {

	
}
