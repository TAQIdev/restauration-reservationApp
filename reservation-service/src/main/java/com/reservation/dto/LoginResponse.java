package com.reservation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long clientId;
    private String name;
    private String email;
}