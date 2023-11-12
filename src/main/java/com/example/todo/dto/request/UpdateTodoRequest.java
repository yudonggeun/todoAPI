package com.example.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateTodoRequest(

        @Schema(description = "할일 식별자", example = "1")
        @NotNull(message = "변경할 할일의 식별자가 필요합니다.")
        Long id,
        @Schema(description = "할일 제목", example = "스프링 정복하기")
        String title,
        @Schema(description = "할일 내용", example = "스프링 모든 것을 공부하기")
        String content
) {
}
