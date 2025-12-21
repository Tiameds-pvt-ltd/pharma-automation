package com.pharma.service.impl;

import com.pharma.entity.UserOtpEntity;
import com.pharma.repository.UserOtpRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpAttemptService {

    private final UserOtpRepository userOtpRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UserOtpEntity otpEntity) {
        otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
        userOtpRepository.save(otpEntity);
    }
}
