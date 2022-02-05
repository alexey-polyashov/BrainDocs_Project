package com.braindocs.braindocs.models.files;

import com.braindocs.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="files")
@Data
@NoArgsConstructor
public class FilesModel {

    @Column(name="storagetype")
    private Integer storageType;

    @Column(name="name")
    private String name;

    @Column(name="describtion")
    private String describtion;

    @Column(name="filesize")
    private Long fileSize;

    @Column(name="filetype")
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author")
    private UserModel author;

    @Column(name="content")
    private String content;

    @Column(name="sourcepath")
    private String sourcePath;

    @Column(name="storagepath")
    private String storagePath;

    @Lob
    @Column(name="filedata")
    private byte[] fileData;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    private Boolean marked;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;

}
