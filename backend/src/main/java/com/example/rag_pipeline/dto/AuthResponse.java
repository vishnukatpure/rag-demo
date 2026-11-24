package com.example.rag_pipeline.dto;

public record AuthResponse(String token, String tokenType, String expiresAt, UserProfile user) {

}
