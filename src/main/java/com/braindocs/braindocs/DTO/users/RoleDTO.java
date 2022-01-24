package com.braindocs.braindocs.DTO.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoleDTO {
    private String name;
    private Long id;
    private Boolean marked;
}
