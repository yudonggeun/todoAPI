package com.example.todo.common.util;

import com.example.todo.dto.CustomerInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TYPE = "REFRESH";
    public static final String ACCESS_TYPE = "ACCESS";

    private final long accessTokenTime;
    private final long refreshTokenTime;

    private final Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Autowired
    public JwtUtil(
            @Value("${jwt.accessToken.duration}")
            long accessTokenTime,
            @Value("${jwt.refreshToken.duration}")
            long refreshTokenTime,
            @Value("${jwt.secret.key}")
            String secretKey
    ) {
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String username, UserRole role, String type) {
        long duration = 0;

        if (type.equals(REFRESH_TYPE)) {
            duration = refreshTokenTime;
        } else if (type.equals(ACCESS_TYPE)) {
            duration = accessTokenTime;
        }

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .claim("type", type)
                        .setExpiration(new Date(new Date().getTime() + duration)) // 만료 시간
                        .setIssuedAt(new Date()) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT 토큰 substring
    private String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큭 추출하기
    public Optional<CustomerInfo> getBearerToken(String token, String type) {
        if (token == null || !token.startsWith("Bearer")) return Optional.empty();
        token = substringToken(token);
        if (!validateToken(token)) return Optional.empty();

        Claims jwt = getUserInfoFromToken(token);

        String username = jwt.getSubject();
        UserRole role = UserRole.valueOf(jwt.get(AUTHORIZATION_KEY, String.class));
        String tokenType = jwt.get("type", String.class);

        if(!tokenType.equals(type)) return Optional.empty();

        return Optional.of(new CustomerInfo(username, role));
    }
}