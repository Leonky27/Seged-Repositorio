package com.proyecto.seged.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;

    // Lee la clave desde application.yml/properties o variable de entorno JWT_SECRET
    public JwtService(@Value("${jwt.secret:}") String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret no configurado (jwt.secret / JWT_SECRET)");
        }

        // 1) Intenta decodificar como Base64 estándar
        byte[] keyBytes = tryBase64(secret);
        if (keyBytes == null) {
            // 2) Si no es Base64, trátalo como TEXTO PLANO (UTF-8)
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        // 3) Garantiza longitud mínima de 32 bytes (256 bits) para HMAC-SHA
        if (keyBytes.length < 32) {
            keyBytes = sha256(keyBytes); // estira la clave de forma determinista
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private static byte[] tryBase64(String s) {
        try {
            return Base64.getDecoder().decode(s);
        } catch (IllegalArgumentException e1) {
            try {
                // También acepta Base64 URL-safe (con '-' y '_')
                return Base64.getUrlDecoder().decode(s);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    private static byte[] sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo inicializar SHA-256", e);
        }
    }

    // === API de ejemplo: ajusta a tu firma actual ===
    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(7, ChronoUnit.DAYS))) // 7 días; cámbialo si quieres
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            String subject = extractUsername(token);
            return subject != null && subject.equals(expectedUsername)
                    && !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }
}
