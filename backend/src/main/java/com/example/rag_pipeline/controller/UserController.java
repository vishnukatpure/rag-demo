package com.example.rag_pipeline.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag_pipeline.dto.UserProfile;
import com.example.rag_pipeline.model.User;
import com.example.rag_pipeline.services.UserService;
import com.example.rag_pipeline.services.generic.GenericService;

@RestController
@RequestMapping("/users")
public class UserController extends GenericService {

	public UserController(UserService userService) {
		super(userService);
	}

	@GetMapping("/me")
	public ResponseEntity<?> me() {
		User user = getLoggedInUser();
		if (user == null) {
			return ResponseEntity.status(401).body(Map.of("error", "User is not authenticated."));
		}
		return ResponseEntity.ok(UserProfile.from(user));
	}

}
