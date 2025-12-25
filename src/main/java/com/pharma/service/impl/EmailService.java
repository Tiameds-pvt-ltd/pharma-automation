package com.pharma.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;



    @Async
    public void sendOtp(String to, String otp) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Your One-Time Password (OTP) for Pharmacy Management System");
        mail.setText(
                "Your OTP: " + otp + "\n\n" +
                        "This OTP is valid for 5 minutes and can be used only once.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "— TiaMeds Team"
        );

        mailSender.send(mail);
    }
}
