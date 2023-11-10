package com.example.todo.controller;

import com.example.todo.common.util.JwtUtil;
import com.example.todo.dto.LoginRequest;
import com.example.todo.dto.MessageResponse;
import com.example.todo.dto.SignUpRequest;
import com.example.todo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var customer = userService.getCustomerInfo(request);
        String token = jwtUtil.createToken(customer.username(), customer.userRole());
        response.setHeader("Authorization", token);
        return ResponseEntity.ok(new MessageResponse("success", "로그인 성공"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            userService.signUp(request);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("error", "중복된 유저 이름을 사용할 수 없습니다."));
        }
        return ResponseEntity.ok().build();
    }
}
