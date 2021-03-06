package com.braindocs.models.tasks;

import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.files.FileModel;
import com.braindocs.models.users.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class TaskModel {

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskTypeModel type;

    @Column(name = "heading")
    private String heading;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Long status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserModel author;

    @ManyToMany
    @JoinTable(name = "task_subjects",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<DocumentModel> subjects;

    @ManyToMany
    @JoinTable(name = "tasks_files",
            joinColumns = @JoinColumn(name = "ownerid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fileid"))
    private Set<FileModel> files;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    @ColumnDefault("false")
    private Boolean marked = false;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateTime;

}
