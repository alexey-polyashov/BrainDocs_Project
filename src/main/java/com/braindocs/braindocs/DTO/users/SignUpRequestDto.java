package com.braindocs.braindocs.DTO.users;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
}