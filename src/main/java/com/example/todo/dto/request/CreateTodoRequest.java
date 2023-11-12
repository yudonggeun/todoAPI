package com.example.todo.dto.request;

import com.example.todo.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequest(
        @Schema(description = "할일 제목", example = "스프링 정복하기")
        @NotEmpty(message = "제목은 필수입니다.")
        String title,
        @Schema(description = "할일 상세 내용", example = "spring mvc, security, jpa 정복!")
        String content
) {
    public Todo toEntity(String author) {
        return Todo.builder()
                .author(author)
                .content(content)
                .title(title)
                .build();
    }
}
