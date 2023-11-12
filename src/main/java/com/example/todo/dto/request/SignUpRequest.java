package com.example.todo.dto.request;

import com.example.todo.validation.Password;
import com.example.todo.validation.Username;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpRequest(
        @Schema(description = "유저이름은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야합니다.", example = "youdong98")
        @Username
        String username,
        @Schema(description = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야합니다.", example = "123aAadfdff")
        @Password
        String password
) {
}
