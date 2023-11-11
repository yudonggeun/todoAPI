package com.example.todo.dto;

import com.example.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoInfo(
        Long id,
        String author,
        String title,
        String content,
        LocalDateTime createdAt
) {

    public static TodoInfo of(Todo todo) {
        return new TodoInfo(
                todo.getId(),
                todo.getAuthor(),
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt()
        );
    }
}
