package com.example.todo.dto;

import com.example.todo.domain.Comment;

public record CommentInfo(
        String author,
        String content,
        Long todoId,
        Long id
) {
    public static CommentInfo of(Comment c) {
        return new CommentInfo(
                c.getAuthor(),
                c.getContent(),
                c.getTodo().getId(),
                c.getId()
        );
    }
}
