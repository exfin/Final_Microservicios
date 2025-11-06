package com.users.microreports.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {
    private static final String SECRET = "cea8f906828900a4bdbe8a6e7366cde4bcae206fd43f1bd96548fb1002ac0039";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Long extractUserId(String token) {
        try {
            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(cleanToken)
                    .getBody();

            return claims.get("id", Long.class);
        } catch (Exception e) {
            System.out.println("No se pudo extraer userId del token: " + e.getMessage());
            return null;
        }
    }
}
