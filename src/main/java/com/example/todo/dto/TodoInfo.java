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

    public TodoInfo hidePrivateColumn(String accessCustomer) {
        if(!this.author.equals(accessCustomer)){
            return new TodoInfo(id, author, title, null, createdAt);
        }
        return this;
    }
}
