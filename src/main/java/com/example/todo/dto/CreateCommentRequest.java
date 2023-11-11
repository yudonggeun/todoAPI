package com.example.todo.dto;

public record CreateCommentRequest(
        Long todoId,
        String content
) {
}
