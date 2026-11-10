package com.example.rag_pipeline.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag_pipeline.dto.AnswerResponse;
import com.example.rag_pipeline.dto.AskRequest;
import com.example.rag_pipeline.services.rag.RagService;

@RestController
@RequestMapping("/rag")
public class RagController {

	private final RagService ragService;

	public RagController(RagService ragService) {
		this.ragService = ragService;
	}

	@PostMapping("/ask")
	public AnswerResponse ask(@RequestBody AskRequest request) {
		if (request == null || request.question() == null || request.question().isBlank()) {
			return new AnswerResponse("Please provide a question.");
		}
		String answer = ragService.ask(request.question());
		return new AnswerResponse(answer);
	}

}
