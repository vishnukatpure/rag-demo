package com.example.rag_pipeline.services.generic;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.rag_pipeline.dto.ResponseDTO;
import com.example.rag_pipeline.enums.StatusEnum;
import com.example.rag_pipeline.model.User;
import com.example.rag_pipeline.services.UserService;

import tools.jackson.databind.ObjectMapper;

public abstract class GenericService {

	private final ModelMapper mapper = new ModelMapper();

	private final ObjectMapper objectMapper = new ObjectMapper();

	final UserService userService;

	protected GenericService(UserService userService) {
		this.userService = userService;
	}

	public ResponseDTO bindResponse(Object dto) {
		return new ResponseDTO().message("Success").object(dto).status(StatusEnum.SUCCESS);
	}

	public ModelMapper getMapper() {
		return mapper;
	}

	public User getLoggedInUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof User user) {
			return user;
		}
		if (principal instanceof UserDetails userDetails) {
			return userService.findByUsername(userDetails.getUsername());
		}
		return null;
	}

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public String objectToJsonString(Object object) {
		try {
			return getObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to serialize object to JSON", e);
		}
	}

	public UserService getUserService() {
		return userService;
	}

}