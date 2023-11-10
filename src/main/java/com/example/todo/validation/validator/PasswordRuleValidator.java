package com.example.todo.validation.validator;

import com.example.todo.validation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordRuleValidator implements ConstraintValidator<Password, String> {

    private static final Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        if (password.length() < 8 || password.length() > 15) return false;
        return pattern.matcher(password).matches();
    }
}
