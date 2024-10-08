package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class InvalidatedTokenCleanupTask {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    // Task chạy mỗi ngày lúc 00:00 để xóa token đã hết hạn
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpiredTokens() {
        Date now = new Date();
        invalidatedTokenRepository.deleteExpiredTokens(now);
    }
}
