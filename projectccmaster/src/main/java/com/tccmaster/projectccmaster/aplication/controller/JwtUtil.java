// Crie este novo arquivo em:
// src/main/java/com/tccmaster/projectccmaster/aplication/controller/JwtUtil.java
package com.tccmaster.projectccmaster.aplication.controller;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String PREFIX = "fake-jwt-token-for-";

    // Extrai o email do nosso token simples. Ex: "fake-jwt-token-for-user@email.com" -> "user@email.com"
    public String extractEmail(String token) {
        if (token != null && token.startsWith(PREFIX)) {
            return token.substring(PREFIX.length());
        }
        return null;
    }

    // Valida se o token é do nosso formato simples
    public boolean validateToken(String token, String userEmail) {
        String emailFromToken = extractEmail(token);
        return (userEmail.equals(emailFromToken));
    }
}