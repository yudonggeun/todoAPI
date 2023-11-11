package com.example.todo.validation;

import com.example.todo.validation.validator.UsernameRuleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameRuleValidator.class)
public @interface Username{

    String message() default "유저 이름은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a-z), 숫자(0~9)로 구성되어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

