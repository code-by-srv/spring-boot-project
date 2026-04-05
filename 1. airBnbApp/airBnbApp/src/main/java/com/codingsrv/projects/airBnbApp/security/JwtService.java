package com.codingsrv.projects.airBnbApp.security;

import com.codingsrv.projects.airBnbApp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null while generating JWT");
        }

        return Jwts.builder()
                    .subject(user.getId().toString())
                    .claim("email", user.getEmail())
                    .claim("roles", Set.of("ADMIN", "USER"))
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 60 * 1000*10))  // 10 minutes
                    .signWith(getSecretKey())
                    .compact();
    }

    public String generateRefreshToken(User user) {

        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null while generating JWT");
        }

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60L*1000*60*24*30*6))  // 6 months
                .signWith(getSecretKey())
                .compact();
    }


    public boolean validateToken(String token, User user) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.valueOf(claims.getSubject());
        Date expiration = claims.getExpiration();

        return userId.equals(user.getId()) && expiration.after(new Date());
    }


    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }


}
