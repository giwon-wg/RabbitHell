package com.example.rabbithell.auth.port.out;

public interface JwtTokenPort {

    String generateAccessToken(String userId, String role);

    String generateRefreshToken(String userId);

    boolean validateToken(String token);

    String extractSubject(String token);

    String extractRole(String token);

}
