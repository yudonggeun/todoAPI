package com.example.todo.repository;

import com.example.todo.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsernameAndPassword(String username, String password);
}
