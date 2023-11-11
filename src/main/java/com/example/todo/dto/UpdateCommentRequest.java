package com.example.todo.dto;

public record UpdateCommentRequest(
        Long id,
        String content
) {
}
