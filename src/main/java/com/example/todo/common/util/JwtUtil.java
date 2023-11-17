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

/*
강의의 JwtUtil를 복사해서 조금 수정한 클래스입니다.
패키지 구성할 때 뭔가 깔끔하게 정리하지 못한 것 같은데요. 어떤 식으로 정리하는 것이 좋을까요?
추가로 같은 패키지의 UserRole의 위치도 조금 꺼림직합니다.
 */
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

    private String removePrefix(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException e){
            return false;
        }
    }

    public Claims getCustomerClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰에 담긴 고객의 정보를 매핑하는 함수입니다.
     * 토큰이 유효하지 않거나 로직에서 원하는 타입이 아닌 경우에는 empty를 반환합니다.
     * @param token Bearer jwtToken...
     * @param type refresh, access 토큰 타입
     * @return 고객이 정보가 담긴 Optinal 객체
     */
    public Optional<CustomerInfo> getCustomerInfoFrom(String token, String type) {
        if (token == null || !token.startsWith("Bearer")) return Optional.empty();

        token = removePrefix(token);
        if (!validateToken(token)) return Optional.empty();

        Claims jwt = getCustomerClaim(token);

        String username = jwt.getSubject();
        UserRole role = UserRole.valueOf(jwt.get(AUTHORIZATION_KEY, String.class));
        String tokenType = jwt.get("type", String.class);

        if(!tokenType.equals(type)) return Optional.empty();

        return Optional.of(new CustomerInfo(username, role));
    }
}