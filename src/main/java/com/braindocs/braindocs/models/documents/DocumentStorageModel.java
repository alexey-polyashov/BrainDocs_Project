package com.braindocs.braindocs.models.documents;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "documents_storage")
@Data
@NoArgsConstructor
public class DocumentStorageModel {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "type")
//    private DocumentTypeModel documentType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private String documentData;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;
}
