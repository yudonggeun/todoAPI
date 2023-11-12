package com.example.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TodoSearchParam(
        @Schema(description = "검색 조건 1 : 할일 제목", example = "스프링")
        String title
) {
}
