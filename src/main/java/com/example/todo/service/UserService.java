package com.example.todo.service;

import com.example.todo.domain.User;
import com.example.todo.dto.SignUpRequest;
import com.example.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignUpRequest request) {
        userRepository.save(User.builder()
                .password(request.password())
                .username(request.username())
                .build());
    }
}
