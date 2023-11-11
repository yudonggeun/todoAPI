package com.example.todo.dto;

public record CreateTodoRequest(
        String title,
        String content
) {
}
