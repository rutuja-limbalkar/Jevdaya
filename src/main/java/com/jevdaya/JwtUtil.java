package com.jevdaya;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    // Generate Token
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        claims.put("roles", roles);
        claims.put("name", user.getName());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract Email 
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ FIXED extractRoles - This is the only change needed
    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // JJWT returns ArrayList, so we safely convert it to Set
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List) {
            List<String> rolesList = (List<String>) rolesObj;
            return rolesList.stream().collect(Collectors.toSet());
        }

        if (rolesObj instanceof Set) {
            return (Set<String>) rolesObj;
        }

        return Set.of(); // return empty set if nothing found
    }
}