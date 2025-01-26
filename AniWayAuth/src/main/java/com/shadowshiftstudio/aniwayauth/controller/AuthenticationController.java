package com.shadowshiftstudio.aniwayauth.controller;

import com.shadowshiftstudio.aniwayauth.dto.AuthenticationRequest;
import com.shadowshiftstudio.aniwayauth.dto.AuthenticationResponse;
import com.shadowshiftstudio.aniwayauth.dto.RefreshTokenRequest;
import com.shadowshiftstudio.aniwayauth.dto.RegisterRequest;
import com.shadowshiftstudio.aniwayauth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for username: {}", request.getUsername());
        AuthenticationResponse response = service.register(request);
        log.info("User registered successfully: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Received login request for username: {}", request.getUsername());
        AuthenticationResponse response = service.authenticate(request);
        log.info("User authenticated successfully: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received refresh token request");
        AuthenticationResponse response = service.refreshToken(refreshTokenRequest.getToken());
        log.info("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }
}
