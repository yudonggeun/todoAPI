package com.example.todo.service;

import com.example.todo.security.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("로그인 상태 서비스 테스트")
class LoginStatusServiceTest {

    LoginStatusService loginStatusService = new LoginStatusService();

    @BeforeEach
    void clearSecurityContext(){
        SecurityContextHolder.clearContext();
    }
    @DisplayName("인증이 되었다면 인증된 유저의 이름을 반환한다.")
    @Test
    void when_set_authentication_then_return_username() {
        // given
        var user = User.builder()
                .username("user")
                .password("1234")
                .build();
        Authentication auth = new JwtToken(user, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // when
        String loginUserName = loginStatusService.getLoginCustomerName();
        // then
        assertThat(loginUserName).isEqualTo("user");
    }

    @DisplayName("인증이 되지 않았다면 에러를 반환한다.")
    @Test
    void when_do_not_authenticate_then_throw_error() {
        // when // then
        assertThatThrownBy(() -> loginStatusService.getLoginCustomerName())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인을 해주세요.");
    }
}