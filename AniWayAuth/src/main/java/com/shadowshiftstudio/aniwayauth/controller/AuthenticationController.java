package com.shadowshiftstudio.aniwayauth.controller;

import com.shadowshiftstudio.aniwayauth.dto.AuthenticationRequest;
import com.shadowshiftstudio.aniwayauth.dto.AuthenticationResponse;
import com.shadowshiftstudio.aniwayauth.dto.RefreshTokenRequest;
import com.shadowshiftstudio.aniwayauth.dto.RegisterRequest;
import com.shadowshiftstudio.aniwayauth.entity.RefreshToken;
import com.shadowshiftstudio.aniwayauth.exception.RefreshTokenNotFoundException;
import com.shadowshiftstudio.aniwayauth.exception.UserNotFoundException;
import com.shadowshiftstudio.aniwayauth.service.AuthenticationService;
import com.shadowshiftstudio.aniwayauth.service.JwtService;
import com.shadowshiftstudio.aniwayauth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws RefreshTokenNotFoundException {
        String refreshToken = refreshTokenRequest.getToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return AuthenticationResponse
                            .builder()
                            .accessToken(accessToken)
                            .token(refreshToken)
                            .build();
                }).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
    }
}
