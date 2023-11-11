package com.example.todo.common.util;

import com.example.todo.dto.CustomerInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.todo.common.util.JwtUtil.ACCESS_TYPE;
import static com.example.todo.common.util.JwtUtil.REFRESH_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    JwtUtil util = new JwtUtil(10000000, 100000000, "testdsaedfsadddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");

    @DisplayName("토큰 생성시 Bearer prefix가 존재한다.")
    @Test
    void test1() {
        // given // when
        String token = util.createToken("user1", UserRole.USER, ACCESS_TYPE);
        // then
        assertThat(token).startsWith("Bearer ");
    }

    @DisplayName("액세스 jwt 토큰 추출시 ACCESS_TYPE 아니라면 null이다.")
    @Test
    void test2() {
        // given
        String token = util.createToken("user1", UserRole.USER, ACCESS_TYPE);
        // when
        Optional<CustomerInfo> bearerToken = util.getBearerToken(token, REFRESH_TYPE);
        // then
        assertThat(bearerToken).isEmpty();
    }

    @DisplayName("refresh token 추출시 REFRESH_TYPE 아니라면 null이다.")
    @Test
    void test3() {
        // given
        String token = util.createToken("user1", UserRole.USER, REFRESH_TYPE);
        // when
        Optional<CustomerInfo> bearerToken = util.getBearerToken(token, ACCESS_TYPE);
        // then
        assertThat(bearerToken).isEmpty();
    }

    @DisplayName("refresh token 추출시 REFRESH_TYPE이면 고객 정보를 반환한다.")
    @Test
    void test4() {
        // given
        String token = util.createToken("user1", UserRole.USER, REFRESH_TYPE);
        // when
        Optional<CustomerInfo> bearerToken = util.getBearerToken(token, REFRESH_TYPE);
        // then
        assertThat(bearerToken).isPresent()
                .get().extracting("name", "userRole")
                .containsExactly("user1", UserRole.USER);
    }

    @DisplayName("access token 추출시 ACCESS_TYPE이면 고객 정보를 반환한다.")
    @Test
    void test5() {
        // given
        String token = util.createToken("user1", UserRole.USER, ACCESS_TYPE);
        // when
        Optional<CustomerInfo> bearerToken = util.getBearerToken(token, ACCESS_TYPE);
        // then
        assertThat(bearerToken).isPresent()
                .get().extracting("name", "userRole")
                .containsExactly("user1", UserRole.USER);
    }
}