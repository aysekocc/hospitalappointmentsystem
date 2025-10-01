package com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;   // JWT token
    private String role;    // ROLE_USER veya ROLE_DOCTOR
    private String message; // Başarılı veya hata mesajı
    private Long userId;    // User veya Doctor ID
}

