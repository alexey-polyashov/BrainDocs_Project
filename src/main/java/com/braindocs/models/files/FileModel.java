package com.braindocs.models.files;

import com.braindocs.models.users.UserModel;
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
public class FileModel {

    @Column(name="storagetype")
    private Integer storageType;

    @Column(name="name")
    private String name;

    @Column(name="filedescription")
    private String description;

    @Column(name="filesize")
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author")
    private UserModel author;

    @Column(name="parsedtext")
    private String parsedText;

    @Column(name="originalfilename")
    private String originalFilename;

    @Column(name="filetype")
    private String fileType;

    @Column(name="contenttype")
    private String contentType;

    @Column(name="storagepath")
    private String storagePath;

    @Lob
    @Column(name="filedata")
    @Basic(fetch=FetchType.LAZY)
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
