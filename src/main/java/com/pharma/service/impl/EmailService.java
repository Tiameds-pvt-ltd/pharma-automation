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
        mail.setSubject("Your OTP for Registration");
        mail.setText(
                "Dear User,\n\n" +
                        "Thank you for registering with our Pharmacy Management System.\n\n" +
                        "To complete your account verification, please use the One-Time Password (OTP) below:\n\n" +
                        "OTP: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes and can be used only once.\n\n" +
                        "If you did not initiate this request, please ignore this email. No changes will be made to your account.\n\n" +
                        "For your security, please do not share this OTP with anyone.\n\n" +
                        "Regards,\n" +
                        "Pharmacy Support Team\n" +
                        "TiaMeds"
        );

        mailSender.send(mail);
    }
}
