package com.example.todo.controller;

import com.example.todo.common.util.JwtUtil;
import com.example.todo.common.util.UserRole;
import com.example.todo.dto.CustomerInfo;
import com.example.todo.dto.request.LoginRequest;
import com.example.todo.dto.request.SignUpRequest;
import com.example.todo.dto.response.AuthorizationResponse;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import static com.example.todo.common.util.JwtUtil.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var customer = userService.getCustomerInfo(request);
        String accessToken = jwtUtil.createToken(customer.name(), customer.role(), ACCESS_TYPE);
        String refreshToken = jwtUtil.createToken(customer.name(), customer.role(), REFRESH_TYPE);

        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        return ResponseEntity.ok(new AuthorizationResponse(
                customer.name(),
                accessToken,
                refreshToken)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            userService.signUp(request);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("error", "중복된 username 입니다."));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        String token = request.getHeader(AUTHORIZATION_HEADER);

        CustomerInfo customerInfo = jwtUtil.getCustomerInfoFrom(token, REFRESH_TYPE)
                .orElseThrow(() -> new AccessDeniedException("토큰이 유효하지 않습니다."));

        String name = customerInfo.name();
        UserRole role = customerInfo.role();

        String accessToken = jwtUtil.createToken(name, role, ACCESS_TYPE);
        String refreshToken = request.getHeader(AUTHORIZATION_HEADER);

        return ResponseEntity.ok(new AuthorizationResponse(
                name,
                accessToken,
                refreshToken)
        );
    }
}
