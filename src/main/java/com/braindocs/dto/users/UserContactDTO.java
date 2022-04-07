package com.braindocs.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserContactDTO {
    private Long userId;
    private String userName;
    private Long typeId;
    private String typeName;
    private String present;
    private Long id;
}
