package com.example.todo.dto;

import com.example.todo.common.util.UserRole;
import com.example.todo.domain.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerInfo(
        @Schema(description = "유저 이름", example = "youdong98")
        String name,
        @Schema(description = "유저 권한", example = "사용자 권한 정보")
        UserRole role
) {
    public static CustomerInfo of(Customer customer) {
        return new CustomerInfo(
                customer.getUsername(),
                getRole(customer)
        );
    }

    private static UserRole getRole(Customer customer) {
        return customer.getAuthority().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(customer.getId() + " 고객의 권한이 존재하지 않습니다."))
                .getRole();
    }
}
