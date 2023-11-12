package com.example.todo.dto;

import com.example.todo.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentInfo(
        @Schema(description = "댓글 작성자", example = "youdong98")
        String author,
        @Schema(description = "댓글 내용", example = "댓글입니다.")
        String content,
        @Schema(description = "댓글 작성된 할일 식별자", example = "1")
        Long todoId,
        @Schema(description = "댓글 식별자", example = "1")
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
