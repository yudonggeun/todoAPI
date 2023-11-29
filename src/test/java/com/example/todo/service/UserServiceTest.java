package com.example.todo.service;

import com.example.todo.IntegrationTest;
import com.example.todo.domain.Authority;
import com.example.todo.domain.Customer;
import com.example.todo.dto.CustomerInfo;
import com.example.todo.dto.request.LoginRequest;
import com.example.todo.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.todo.common.util.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayName("유저 서비스 테스트")
class UserServiceTest extends IntegrationTest {

    @Autowired
    UserService userService;

    @DisplayName("회원가입 성공시 고객의 정보를 조회할 수 있다.")
    @Test
    void when_signup_then_present_user() {
        // given
        var request = new SignUpRequest("user1234", "a234Afdfd");
        // when
        userService.signUp(request);
        Optional<Customer> signupUser = customerRepository.findByUsernameAndPassword(request.username(), request.password());
        // then
        assertThat(signupUser).isPresent();
    }

    @DisplayName("동일한 이름으로 회원가입시 에러가 발생한다.")
    @Test
    void do_signup_when_duplicated_username_then_throw_IllegalArgumentException() {
        // given
        customerRepository.save(Customer.builder()
                .username("user1234")
                .password("sdafajsldkfj234D")
                .authority(List.of(new Authority(USER)))
                .build());

        var request = new SignUpRequest("user1234", "a234Afdfd");
        // when // then
        assertThatThrownBy(() -> userService.signUp(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 유저 이름은 사용할 수 없습니다.");
    }


}