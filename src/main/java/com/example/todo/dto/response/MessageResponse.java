package com.example.todo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageResponse(
        @Schema(description = "응답 상태", example = "success")
        String status,
        @Schema(description = "응답 메시지", example = "요청이 성공했습니다.")
        String message
) {
}
