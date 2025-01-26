package com.shadowshiftstudio.aniwayauth.service;

import com.shadowshiftstudio.aniwayauth.dto.AuthenticationRequest;
import com.shadowshiftstudio.aniwayauth.dto.AuthenticationResponse;
import com.shadowshiftstudio.aniwayauth.dto.RegisterRequest;
import com.shadowshiftstudio.aniwayauth.entity.RefreshToken;
import com.shadowshiftstudio.aniwayauth.entity.User;
import com.shadowshiftstudio.aniwayauth.exception.*;
import com.shadowshiftstudio.aniwayauth.repository.PasswordResetTokenRepository;
import com.shadowshiftstudio.aniwayauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private PasswordEncoder passwordEncoder;
    private UserRepository repository;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private AuthenticationManager authenticationManager;
    private LoginLogService loginLogService;

    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository repository, JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, LoginLogService loginLogService) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.loginLogService = loginLogService;
    }

    public AuthenticationResponse register(RegisterRequest request) throws UsernameIsOccupiedException, EmailIsOccupiedException, UserNotFoundException {
        validateRegisterRequest(request);

        var user = User.builder()
                .username(request.getUsername())
                .role(request.getRole())
                .email(request.getEmail())
                .password_hash(passwordEncoder.encode(request.getPassword()))
                .createdAt(new Date(System.currentTimeMillis()).toInstant())
                .build();

        repository.save(user);

        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        loginLogService.saveLoginLog(user.getId());

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .token(refreshToken.getToken())
                .build();
    }

    private void validateRegisterRequest(RegisterRequest request) throws EmailIsOccupiedException, UsernameIsOccupiedException {
        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new EmailIsOccupiedException("Account with this email is already registered");
        if (repository.findByUsername(request.getUsername()).isPresent())
            throw new UsernameIsOccupiedException("Account with this username is already registered");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        loginLogService.saveLoginLog(user.getId());

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .token(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) throws RefreshTokenNotFoundException {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshToken)
                            .build();
                }).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
    }
}
