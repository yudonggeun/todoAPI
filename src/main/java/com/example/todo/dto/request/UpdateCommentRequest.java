package com.example.todo.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @NotNull(message = "댓글 식별자가 필요합니다.")
        Long id,
        String content
) {
}
