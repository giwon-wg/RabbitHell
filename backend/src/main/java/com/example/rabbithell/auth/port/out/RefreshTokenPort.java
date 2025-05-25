package com.example.rabbithell.auth.port.out;

import java.util.Optional;

public interface RefreshTokenPort {

    void save(String userId, String refreshToken);

    Optional<String> getByUserId(String userId);

    void delete(String userId);

}
