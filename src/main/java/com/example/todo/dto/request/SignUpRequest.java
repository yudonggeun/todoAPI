package com.example.todo.dto.request;

import com.example.todo.validation.Password;
import com.example.todo.validation.Username;

public record SignUpRequest(
        @Username
        String username,
        @Password
        String password
) {
}
