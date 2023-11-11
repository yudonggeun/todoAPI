package com.example.todo.validation.validator;

import com.example.todo.validation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UsernameRuleValidator implements ConstraintValidator<Username, String> {

    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])[a-z0-9]+$");

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(username == null) return false;
        if (username.length() < 4 || username.length() > 10) return false;
        return pattern.matcher(username).matches();
    }
}
