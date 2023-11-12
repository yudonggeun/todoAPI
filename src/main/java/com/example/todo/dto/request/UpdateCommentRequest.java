package com.example.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @Schema(description = "댓글 식별자", example = "1")
        @NotNull(message = "댓글 식별자가 필요합니다.")
        Long id,
        @Schema(description = "변경 내용", example = "수정했습니다.")
        String content
) {
}
