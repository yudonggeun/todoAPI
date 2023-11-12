package com.example.todo.dto.response;

import com.example.todo.dto.TodoInfoEntry;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TodoInfoListResponse(
        @Schema(description = "목록 길이", example = "1")
        long size,
        @Schema(description = "데이터")
        List<TodoInfoEntry> data
) {
}
