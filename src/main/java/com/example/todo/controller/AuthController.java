package com.example.todo.controller;

import com.example.todo.common.util.JwtUtil;
import com.example.todo.common.util.UserRole;
import com.example.todo.dto.CustomerInfo;
import com.example.todo.dto.request.LoginRequest;
import com.example.todo.dto.request.SignUpRequest;
import com.example.todo.dto.response.AuthorizationResponse;
import com.example.todo.dto.response.MessageResponse;
import com.example.todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import static com.example.todo.common.util.JwtUtil.*;

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인", description = "Jwt 토큰 기반 로그인 처리를 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", headers = @Header(name = AUTHORIZATION_HEADER, description = "Bearer jwt 토큰을 정보입니다.")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
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

    @Operation(summary = "회원가입", description = "새로운 유저를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
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

    @Operation(summary = "토큰 재발급", description = "refresh 토큰으로 access 토큰을 재발급 할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
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
