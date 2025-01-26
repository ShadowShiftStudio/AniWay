package com.shadowshiftstudio.databasebridge.repository;

import com.shadowshiftstudio.databasebridge.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    List<LoginLog> findByUserId(Long userId);
}
