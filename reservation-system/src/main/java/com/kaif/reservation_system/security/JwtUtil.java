package com.kaif.reservation_system.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key - must be at least 256 bits (32 characters)
    private static final String SECRET = "your-----secretKeyForJWTMustBeLongEnoughAtLeast32Characters";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate JWT token
    public String generateToken(String email, Integer restaurantId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("restaurantId", restaurantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract restaurant ID from token
    public Integer extractRestaurantId(String token) {
        return extractClaims(token).get("restaurantId", Integer.class);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}