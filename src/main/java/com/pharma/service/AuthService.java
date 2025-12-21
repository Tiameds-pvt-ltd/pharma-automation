package com.pharma.service;

import com.pharma.dto.VerifyOtpDto;
import com.pharma.dto.auth.RegisterRequest;

public interface AuthService {

    void registerInit(RegisterRequest registerRequest);

    void verifyOtpAndRegister(VerifyOtpDto verifyOtpDto);

    void resendOtp(String email);

}
