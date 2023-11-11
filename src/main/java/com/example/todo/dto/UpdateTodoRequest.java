package com.example.todo.dto;

public record UpdateTodoRequest(

        Long id,
        String title,
        String content
) {
}
