package com.pharma.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendLoginOtpRequest {
    @NotBlank
    private String username;
}
