package com.example.todo.service;

import com.example.todo.common.exception.NotExistException;
import com.example.todo.common.util.UserRole;
import com.example.todo.domain.Authority;
import com.example.todo.domain.Customer;
import com.example.todo.dto.CustomerInfo;
import com.example.todo.dto.request.LoginRequest;
import com.example.todo.dto.request.SignUpRequest;
import com.example.todo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;

    public void signUp(SignUpRequest request) {

        if (customerRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("동일한 유저 이름은 사용할 수 없습니다.");
        }
        customerRepository.save(Customer.builder()
                .password(request.password())
                .username(request.username())
                .authority(List.of(new Authority(UserRole.USER)))
                .build());
    }

    public CustomerInfo getCustomerInfo(LoginRequest req) {
        Customer customer = customerRepository
                .findByUsernameAndPassword(req.username(), req.password())
                .orElseThrow(() -> new NotExistException("회원을 찾을 수 없습니다."));
        return CustomerInfo.of(customer);
    }
}