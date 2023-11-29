package com.example.todo.controller;

import com.example.todo.IntegrationTest;
import com.example.todo.common.util.JwtUtil;
import com.example.todo.common.util.UserRole;
import com.example.todo.domain.Authority;
import com.example.todo.domain.Customer;
import com.example.todo.dto.request.LoginRequest;
import com.example.todo.dto.request.SignUpRequest;
import com.example.todo.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Transactional
class AuthControllerTest extends IntegrationTest {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    JwtUtil jwtUtil;

    @AfterEach
    void clear() {
        customerRepository.deleteAll();
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() throws Exception {
        // given
        String username = "testuser";
        String password = "testT12345";

        saveCustomer(username, password);
        var request = new LoginRequest(username, password);
        // when // then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.customerName").value("testuser"),
                jsonPath("$.accessToken").exists(),
                jsonPath("$.refreshToken").exists()
        );
    }

    @DisplayName("로그인 성공")
    @Test
    void loginFailWhenNotFound() throws Exception {
        // given
        String username = "testuser";
        String password = "testT12345";
        var request = new LoginRequest(username, password);
        // when // then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value("bad request"),
                jsonPath("$.message").value("회원을 찾을 수 없습니다.")
        );
    }

    @DisplayName("로그인 실패 : 입력 정보 불일치")
    @Test
    void loginFailWhenInputNotCorrect() throws Exception {
        // given
        String username = "testuser";
        String password = "testT12345";

        saveCustomer(username, password);
        var request = new LoginRequest(username, "testR12345");
        // when // then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value("bad request"),
                jsonPath("$.message").value("회원을 찾을 수 없습니다.")
        );
    }

    @DisplayName("회원가입 성공")
    @Test
    void signupSuccess() throws Exception {
        // given
        String username = "testuser";
        String password = "testT12345";
        var request = new SignUpRequest(username, password);
        // when // then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.status").value("success"),
                jsonPath("$.message").value("회원가입에 성공했습니다.")
        );
    }

    @DisplayName("회원가입 실패 : 중복된 유저이름 사용시")
    @Test
    void signupFailWhenDuplicatedUsername() throws Exception {
        // given
        var username = "testuser";
        var password = "testT12345";
        var request = new SignUpRequest(username, password);
        saveCustomer(username, password);

        // when // then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value("error"),
                jsonPath("$.message").value("중복된 username 입니다.")
        );
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void refresh() throws Exception {
        // given
        String refreshToken = jwtUtil.createToken("testuser", UserRole.USER, JwtUtil.REFRESH_TYPE);
        // when // then
        mockMvc.perform(get("/auth/refresh")
                .header(JwtUtil.AUTHORIZATION_HEADER, refreshToken)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.customerName").value("testuser"),
                jsonPath("$.accessToken").exists(),
                jsonPath("$.refreshToken").exists()
        );
    }

    @DisplayName("토큰 재발급 실패 : 엑세스 토큰일 때")
    @Test
    void refreshWhenTokenIsAccessToken() throws Exception {
        // given
        String refreshToken = jwtUtil.createToken("testuser", UserRole.USER, JwtUtil.ACCESS_TYPE);
        // when // then
        mockMvc.perform(get("/auth/refresh")
                .header(JwtUtil.AUTHORIZATION_HEADER, refreshToken)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value("unauthorized"),
                jsonPath("$.message").value("토큰이 유효하지 않습니다.")
        );
    }

    private Customer saveCustomer(String username, String password) {
        return customerRepository.saveAndFlush(Customer.builder()
                .username(username)
                .password(password)
                .authority(List.of(new Authority(UserRole.USER)))
                .build());
    }
}