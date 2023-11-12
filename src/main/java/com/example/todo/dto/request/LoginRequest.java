package com.example.todo.dto.request;

import com.example.todo.validation.Password;
import com.example.todo.validation.Username;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "로그인 유저이름", example = "youdong98")
        @Username
        String username,
        @Schema(description = "비밀번호", example = "123aA")
        @Password
        String password
) {
}
