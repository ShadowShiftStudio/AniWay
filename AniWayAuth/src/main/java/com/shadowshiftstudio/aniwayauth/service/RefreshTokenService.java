package com.shadowshiftstudio.aniwayauth.service;

import com.shadowshiftstudio.aniwayauth.entity.RefreshToken;
import com.shadowshiftstudio.aniwayauth.repository.RefreshTokenRepository;
import com.shadowshiftstudio.aniwayauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken token = RefreshToken
                .builder()
                .user(userRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpiration)) // 1 hour
                .createdAt(Instant.now())
                .build();
        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired. Please make a new signIn request");
        }
        return token;
    }
}
