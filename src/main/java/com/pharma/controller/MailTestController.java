package com.pharma.controller;

import com.pharma.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final EmailService emailService;

    @GetMapping("/test-mail")
    public String testMail() {
        emailService.sendOtp("sbrao050496@gmail.com", "123456");
        return "Mail sent successfully";
    }
}
