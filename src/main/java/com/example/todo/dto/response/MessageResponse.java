package com.example.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageResponse(
        @Schema(description = "응답 상태", example = "status")
        String status,
        @Schema(description = "응답 메시지", example = "response message")
        String message
) {
}
