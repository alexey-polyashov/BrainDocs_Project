package com.braindocs.DTO.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private String name;
    private Long id;
    private Boolean marked;
}
