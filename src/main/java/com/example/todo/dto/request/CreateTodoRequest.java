package com.example.todo.dto.request;

import com.example.todo.domain.Todo;
import jakarta.validation.constraints.NotEmpty;

public record CreateTodoRequest(
        @NotEmpty(message = "제목은 필수입니다.")
        String title,
        String content
) {
    public Todo toEntity(String author) {
        return Todo.builder()
                .author(author)
                .isComplete(false)
                .content(content)
                .title(title)
                .build();
    }
}
