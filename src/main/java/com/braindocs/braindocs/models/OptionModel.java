package com.braindocs.braindocs.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="options")
@Data
@NoArgsConstructor
public class OptionModel {

    @Id
    private final int id = 1;

    @Column(name="file_storage_type")
    private Integer fileStorageType;

}
