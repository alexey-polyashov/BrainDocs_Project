package com.braindocs.DTO.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserContactDTO {
    private Long userId;
    private String userName;
    private Long typeId;
    private String typeNAme;
    private String present;
    private Long id;
    private Boolean marked;
}
