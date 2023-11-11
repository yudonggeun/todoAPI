package com.example.todo.dto.response;

public record AuthorizationResponse(
        String CustomerName,
        String accessToken,
        String refreshToken
) {
}
