package ru.sfchick.Data_Sphere.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sfchick.Data_Sphere.repositories.VerificationCodeRepository;

import java.time.LocalDateTime;

@Component
public class VerificationCleanupScheduler {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCleanupScheduler(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Scheduled(fixedRate = 600_000) // Запуск каждые 10 минут
    @Transactional
    public void cleanupExpiredVerifications() {
        verificationCodeRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}
