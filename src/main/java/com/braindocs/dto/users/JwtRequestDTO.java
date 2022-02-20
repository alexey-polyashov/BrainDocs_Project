package com.braindocs.dto.users;

import lombok.Data;

@Data
public class JwtRequestDTO {
    private String username;
    private String password;
}
