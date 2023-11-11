package com.example.todo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotNull(message = "할일 식별자가 필요합니다.")
        Long todoId,
        @NotEmpty(message = "빈 댓글은 작성할 수 없습니다.")
        String content
) {
}
