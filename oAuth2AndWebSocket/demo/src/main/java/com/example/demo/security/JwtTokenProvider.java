package com.example.demo.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtTokenProvider {
    @Value("${token.secret}")
    private String SECRET_KEY;

    @Value("${token.expiration_time}")
    private String EXPIRATION_TIME;


    public String createJwt(User user) {
        String jwtToken = JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", user.getId())
                .withClaim("username", user.getName())
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return jwtToken;
    }

    public Long validateAndGetUserId(String token) {
        Long userId = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                .verify(token)
                .getClaim("id")
                .asLong();
        return userId;
    }
}
