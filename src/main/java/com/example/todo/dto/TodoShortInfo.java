package com.example.todo.dto;

import com.example.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TodoShortInfo(

        @Schema(description = "할일 식별자", example = "1")
        Long id,
        @Schema(description = "할일 작성자", example = "youdong98")
        String author,
        @Schema(description = "할일 제목", example = "스프링 정복하기")
        String title,
        @Schema(description = "할일 상세 내용", example = "spring mvc, security, jpa 정복!")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String content,
        @Schema(description = "할일 작성시간", example = "2023-11-11T20:43:18.170")
        LocalDateTime createdAt
) {
    public static TodoShortInfo of(Todo todo) {
        return new TodoShortInfo(
                todo.getId(),
                todo.getAuthor(),
                todo.getTitle(),
                todo.getContent(),
                todo.getCreatedAt()
        );
    }
}
