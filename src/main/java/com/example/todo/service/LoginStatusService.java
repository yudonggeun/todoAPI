package com.example.todo.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginStatusService {

    public String getLoginCustomerName() {
        try {
            return SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
        } catch (RuntimeException ex){
            throw new IllegalArgumentException("로그인을 해주세요.");
        }
    }
}
