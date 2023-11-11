package com.example.todo.dto;

import com.example.todo.common.util.UserRole;
import com.example.todo.domain.Authority;
import com.example.todo.domain.Customer;

public record CustomerInfo(
        String name,
        UserRole userRole
) {
    public static CustomerInfo of(Customer customer) {
        return new CustomerInfo(
                customer.getUsername(),
                getRole(customer)
        );
    }

    private static UserRole getRole(Customer customer) {
        Authority authority = customer.getAuthority().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(customer.getId() + " 고객의 권한이 존재하지 않습니다."));
        return authority.getRole();
    }
}
