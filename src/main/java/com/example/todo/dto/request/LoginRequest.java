package com.example.todo.dto.request;

import com.example.todo.validation.Password;
import com.example.todo.validation.Username;

public record LoginRequest(
        @Username
        String username,
        @Password
        String password
) {
}
