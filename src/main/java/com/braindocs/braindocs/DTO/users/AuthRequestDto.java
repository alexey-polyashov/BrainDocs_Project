package com.braindocs.braindocs.DTO.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDto {
    private String email;
    private String password;
}