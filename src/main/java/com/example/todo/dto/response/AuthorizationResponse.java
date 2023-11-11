package com.example.todo.dto.response;

public record AuthorizationResponse(
        String accessToken,
        String refreshToken
) {
}
