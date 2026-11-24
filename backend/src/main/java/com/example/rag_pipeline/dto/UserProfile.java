package com.example.rag_pipeline.dto;

import com.example.rag_pipeline.model.User;

public record UserProfile(Long id, String firstName, String lastName, String email, String username) {

	public static UserProfile from(User user) {
		return new UserProfile(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getUsername());
	}

}
