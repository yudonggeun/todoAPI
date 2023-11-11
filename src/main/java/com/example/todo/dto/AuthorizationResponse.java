package com.example.todo.dto;

public record AuthorizationResponse(
        String accessToken,
        String refreshToken
) {
}
