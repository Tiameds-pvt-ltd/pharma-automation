package com.pharma.scheduler;

import com.pharma.repository.TempUserRegistrationRepository;
import com.pharma.repository.UserOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OtpCleanupScheduler {

    private final UserOtpRepository userOtpRepository;
    private final TempUserRegistrationRepository tempRepo;
    private static final int MAX_ATTEMPTS = 5;

    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) // every 5 minutes
    public void cleanupExpiredOtpAndRegistrations() {

        LocalDateTime now = LocalDateTime.now();

        userOtpRepository.deleteExpiredOrLockedOtps(now, MAX_ATTEMPTS);
        tempRepo.deleteExpiredRegistrations(now);
    }
}
