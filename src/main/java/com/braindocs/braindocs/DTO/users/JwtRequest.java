package com.braindocs.braindocs.DTO.users;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
