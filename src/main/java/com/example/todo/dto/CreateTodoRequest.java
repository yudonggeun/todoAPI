package com.example.todo.dto;

import com.example.todo.domain.Todo;

public record CreateTodoRequest(
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
