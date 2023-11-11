package com.example.todo.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateTodoRequest(

        @NotNull(message = "변경할 할일의 식별자가 필요합니다.")
        Long id,
        String title,
        String content
) {
}
