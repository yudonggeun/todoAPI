package com.example.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TodoInfoEntry(
        @Schema(description = "할일 작성자", example = "youdong98")
        String author,
        @Schema(description = "할일 목록")
        List<TodoShortInfo> todoList
) {
}
