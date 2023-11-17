package com.example.todo.security;

import com.example.todo.common.util.JwtUtil;
import com.example.todo.common.util.UserRole;
import com.example.todo.dto.CustomerInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.todo.common.util.JwtUtil.ACCESS_TYPE;
import static com.example.todo.common.util.JwtUtil.AUTHORIZATION_HEADER;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(AUTHORIZATION_HEADER);
        Optional<CustomerInfo> bearerToken = jwtUtil.getCustomerInfoFrom(token, ACCESS_TYPE);

        // 유효한 엑세스 토큰인 경우
        if (bearerToken.isPresent()) {
            var customer = bearerToken.get();

            String username = customer.name();
            UserRole role = customer.role();

            UserDetails user = User.withUsername(username)
                    .roles(role.name())
                    .password("")
                    .build();

            // 인가 처리
            var authentication = new JwtToken(user, List.of(new SimpleGrantedAuthority(role.name())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
