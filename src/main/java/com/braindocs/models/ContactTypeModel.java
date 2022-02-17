package com.braindocs.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="contacts_types")
@Data
@NoArgsConstructor
public class ContactTypeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "marked")
    private Boolean marked;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;
}
