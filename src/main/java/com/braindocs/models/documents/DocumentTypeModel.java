package com.braindocs.models.documents;

import com.braindocs.models.files.FileModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="documents_types")
@Data
@NoArgsConstructor
public class DocumentTypeModel {

    @Column(name="name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    @ColumnDefault("false")
    private Boolean marked;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;

    @ManyToMany
    @JoinTable(name ="documenttypes_files",
            joinColumns = @JoinColumn(name = "ownerid", referencedColumnName = "id"),
            inverseJoinColumns =  @JoinColumn(name="fileid"))
    private Set<FileModel> files;

}
