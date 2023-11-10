package com.example.todo.validation.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordRuleValidatorTest {

    PasswordRuleValidator validator = new PasswordRuleValidator();

    @DisplayName("비밀번호가 7자리 이하면 실패다")
    @Test
    void test1() {
        // given // when
        boolean result = validator.isValid("a1234", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 15자리 초과면 실패다")
    @Test
    void test2() {
        // given // when
        boolean result = validator.isValid("a1234a1234a12341", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 알파벳 소문자만 있다면 실패다")
    @Test
    void test3() {
        // given // when
        boolean result = validator.isValid("asdfasdfasdf", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 숫자로만 있다면 실패다")
    @Test
    void test4() {
        // given // when
        boolean result = validator.isValid("123456789", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 대문자로만 있다면 실패다")
    @Test
    void test5() {
        // given // when
        boolean result = validator.isValid("ASDFASDFASDF", null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비밀번호가 대문자 소문자 숫자로 이루어져 있다면 성공")
    @Test
    void test6() {
        // given // when
        boolean result = validator.isValid("ASDFasD13S", null);
        // then
        assertThat(result).isTrue();
    }
}