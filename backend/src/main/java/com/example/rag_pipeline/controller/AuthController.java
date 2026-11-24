package com.example.rag_pipeline.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rag_pipeline.dto.AuthResponse;
import com.example.rag_pipeline.dto.LoginRequest;
import com.example.rag_pipeline.dto.RegisterRequest;
import com.example.rag_pipeline.dto.UserProfile;
import com.example.rag_pipeline.enums.DefaultRoles;
import com.example.rag_pipeline.model.Roles;
import com.example.rag_pipeline.model.User;
import com.example.rag_pipeline.repository.UserRepository;
import com.example.rag_pipeline.services.RolesService;
import com.example.rag_pipeline.services.UserService;
import com.example.rag_pipeline.utils.JwtUtils;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/non-secured/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final UserService userService;
	private final UserRepository userRepository;
	private final RolesService rolesService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService,
			UserRepository userRepository, RolesService rolesService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
		this.userRepository = userRepository;
		this.rolesService = rolesService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		if (request == null || isBlank(request.username()) || isBlank(request.password())) {
			return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required."));
		}

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.username().trim(), request.password()));
			return ResponseEntity.ok(toAuthResponse(authentication, request.username().trim()));
		} catch (AuthenticationException ex) {
			return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password."));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		if (request == null || isBlank(request.email()) || isBlank(request.password())
				|| isBlank(request.firstName()) || isBlank(request.lastName())) {
			return ResponseEntity.badRequest()
					.body(Map.of("error", "First name, last name, email, and password are required."));
		}

		String email = request.email().trim();
		if (userRepository.existsByUsername(email)) {
			return ResponseEntity.status(409).body(Map.of("error", "An account with this email already exists."));
		}

		Roles role = rolesService.findByRole(DefaultRoles.ROLE_USER.name());
		if (role == null) {
			Roles created = new Roles();
			created.setRole(DefaultRoles.ROLE_USER.name());
			rolesService.addAuthorities(created);
			role = rolesService.findByRole(DefaultRoles.ROLE_USER.name());
		}

		userService.addUser(request.firstName().trim(), request.lastName().trim(), email,
				request.sex() == null ? "" : request.sex().trim(), request.password(), role);

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
			return ResponseEntity.ok(toAuthResponse(authentication, email));
		} catch (AuthenticationException ex) {
			return ResponseEntity.status(201).body(Map.of("message", "Account created. Please sign in."));
		}
	}

	private AuthResponse toAuthResponse(Authentication authentication, String username) {
		JsonObject jwt = jwtUtils.generateJwtToken(authentication);
		User user = userService.findByUsername(username);
		return new AuthResponse(jwt.get("token").getAsString(), "Bearer", jwt.get("token-expiry").getAsString(),
				UserProfile.from(user));
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}
