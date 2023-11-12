package com.example.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthorizationResponse(
        @Schema(description = "유저 이름", example = "youdong98")
        String CustomerName,
        @Schema(description = "access 토큰", example = "Bearer xxxx...")
        String accessToken,
        @Schema(description = "refresh 토큰", example = "Bearer xxxx...")
        String refreshToken
) {
}
