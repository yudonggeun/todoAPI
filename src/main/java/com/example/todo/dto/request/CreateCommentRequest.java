package com.example.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @Schema(description = "댓글을 작성하는 할일 식별자", example = "1")
        @NotNull(message = "할일 식별자가 필요합니다.")
        Long todoId,
        @Schema(description = "댓글 내용", example = "댓글입니다.")
        @NotEmpty(message = "빈 댓글은 작성할 수 없습니다.")
        String content
) {
}
