package com.example.todo.dto;

import com.example.todo.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record TodoInfo(
        @Schema(description = "할일 식별자", example = "1")
        Long id,
        @Schema(description = "할일 작성자", example = "youdong98")
        String author,
        @Schema(description = "할일 제목", example = "스프링 정복하기")
        String title,
        @Schema(description = "할일 상세 내용", example = "spring mvc, security, jpa 정복!")
        String content,
        @Schema(description = "할일 작성시간", example = "2023-11-11T20:43:18.170")
        LocalDateTime createdAt,
        @Schema(description = "댓글 목록")
        List<CommentInfo> comments
) {

    public static TodoInfo of(Todo todo) {
        List<CommentInfo> comments = todo.getComments()
                .stream().map(CommentInfo::of)
                .toList();
        return new TodoInfo(
                todo.getId(),
                todo.getAuthor(),
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt(),
                comments
        );
    }

}
