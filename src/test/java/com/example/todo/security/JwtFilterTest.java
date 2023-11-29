package com.example.todo.security;

import com.example.todo.common.util.JwtUtil;
import com.example.todo.dto.CustomerInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static com.example.todo.common.util.JwtUtil.ACCESS_TYPE;
import static com.example.todo.common.util.JwtUtil.AUTHORIZATION_HEADER;
import static com.example.todo.common.util.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("JWT 인증 필터 테스트")
class JwtFilterTest {

    JwtUtil jwtUtil = mock(JwtUtil.class);
    JwtFilter jwtFilter = new JwtFilter(jwtUtil);

    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("유효한 JWT 토큰으로부터 인증 정보를 저장한다.")
    @Test
    void when_valid_token_request_then_save_auth_at_context() throws ServletException, IOException {
        // given
        var username = "test134";
        var token = "test token";
        var request = mock(HttpServletRequest.class);

        given(request.getHeader(AUTHORIZATION_HEADER)).willReturn(token);
        given(jwtUtil.getCustomerInfoFrom(eq(token), eq(ACCESS_TYPE))).willReturn(Optional.of(new CustomerInfo(username, USER)));
        // when
        jwtFilter.doFilterInternal(request, null, mock(FilterChain.class));
        // then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth.getName()).isEqualTo(username);
        assertThat(auth.isAuthenticated()).isTrue();
    }

    @DisplayName("유효하지 않은 토큰이거나 토큰이 없다면 인증 정보는 저장되지 않는다.")
    @Test
    void when_not_exist_token_then_do_not_save_auth() throws ServletException, IOException {
        // given
        Optional<CustomerInfo> notValidTokenInfo = Optional.empty();
        given(jwtUtil.getCustomerInfoFrom(any(), eq(ACCESS_TYPE))).willReturn(notValidTokenInfo);
        // when
        jwtFilter.doFilterInternal(mock(HttpServletRequest.class), null, mock(FilterChain.class));
        // then
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
    }
}