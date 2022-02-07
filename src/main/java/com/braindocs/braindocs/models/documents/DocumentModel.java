package com.braindocs.braindocs.models.documents;

import com.braindocs.braindocs.models.files.FilesModel;
import com.braindocs.braindocs.models.organisations.OrganisationModel;
import com.braindocs.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="documents")
@Data
@NoArgsConstructor
public class DocumentModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private DocumentTypeModel documentType;

    @Column(name="number")
    private String number;

    @Column(name="document_date")
    private Date documentDate;

    @Column(name="heading")
    private String heading;

    @Column(name="content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author")
    private UserModel author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="responsible")
    private UserModel responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="organisation")
    private OrganisationModel organisation;

    @ManyToMany
    @JoinTable(name ="documents_files",
            joinColumns = @JoinColumn(name = "ownerid", referencedColumnName = "id"),
            inverseJoinColumns =  @JoinColumn(name="fileid"))
    private Set<FilesModel> files;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
