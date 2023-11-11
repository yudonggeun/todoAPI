package com.example.todo.validation.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsernameRuleValidatorTest {

    UsernameRuleValidator validator = new UsernameRuleValidator();

    @DisplayName("유저이름 4자리 이하면 실패다")
    @Test
    void test1() {
        // given // when
        boolean result = validator.isValid("a12", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유저이름 10자리 초과면 실패다")
    @Test
    void test2() {
        // given // when
        boolean result = validator.isValid("a1234a1234a12341", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 숫자로만 있다면 실패다")
    @Test
    void test4() {
        // given // when
        boolean result = validator.isValid("1234", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유저이름 대문자면 실패다")
    @Test
    void test5() {
        // given // when
        boolean result = validator.isValid("ASDFAF", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유저이름 소문자, 숫자로 이루어져 있다면 성공")
    @Test
    void test6() {
        // given // when
        boolean result = validator.isValid("as13s", null);
        // then
        assertThat(result).isTrue();
    }
}
