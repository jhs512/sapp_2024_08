package com.ll.sapp.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    @Value("${custom.secret.jwt.secretKey}")
    private String jwtSecretKey;
    @Value("${custom.secret.jwt.expireSeconds}")
    private long jwtExpireSeconds;

    public String genAccessToken(Member member) {
        Claims claims = Jwts
                .claims()
                .add("id", member.getId())
                .add("username", member.getUsername())
                .add("authorities", member.getAuthoritiesAsStringList())
                .build();

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000 * jwtExpireSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public Map<String, Object> getDataFrom(String token) {
        Claims payload = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return Map.of(
                "id", payload.get("id", Integer.class),
                "username", payload.get("username", String.class),
                "authorities", payload.get("authorities", List.class)
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String genRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}