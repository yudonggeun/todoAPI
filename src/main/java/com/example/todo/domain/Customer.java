package com.example.todo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
@Getter
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Authority> authority = new ArrayList<>();


    @Builder
    private Customer(String username, String password, List<Authority> authority) {
        this.username = username;
        this.password = password;
        this.authority.addAll(authority);
    }
}
