package com.shadowshiftstudio.aniwayauth.service;

import com.shadowshiftstudio.aniwayauth.entity.LoginLog;
import com.shadowshiftstudio.aniwayauth.entity.User;
import com.shadowshiftstudio.aniwayauth.repository.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginLogService {
    private final LoginLogRepository loginLogRepository;

    public LoginLog saveLoginLog(Long userId) {
        LoginLog loginLog = LoginLog.builder()
                .user(User.builder().id(userId).build())
                .loginTime(Instant.now())
                .ipAddress("need to add")
                .build();
        return loginLogRepository.save(loginLog);
    }

    public List<LoginLog> getAllLoginLogs() {
        return loginLogRepository.findAll();
    }

    public List<LoginLog> getLoginLogsByUserId(Long userId) {
        return loginLogRepository.findByUserId(userId);
    }
}