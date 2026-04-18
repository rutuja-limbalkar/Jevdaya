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

	// This is a fixed 64-character (512-bit) secret key
	private static final String SECRET_STRING = "JeevdayaAdminPanelSecretKey2026_Secure_Fixed_String_For_HS512_Auth";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

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

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object rolesObj = claims.get("roles");

            if (rolesObj instanceof List) {
                return ((List<String>) rolesObj).stream().collect(Collectors.toSet());
            }
            if (rolesObj instanceof Set) {
                return (Set<String>) rolesObj;
            }
            return Set.of();
        } catch (Exception e) {
            return Set.of();
        }
    }
}