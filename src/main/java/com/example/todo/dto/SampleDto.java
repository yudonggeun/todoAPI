package com.example.todo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record SampleDto(
        Long id,
        String password,
        @NotNull
        @Pattern(regexp = "test product name")
        String productName,
        int quantity,
        long price,
        List<String> items
) {
}

