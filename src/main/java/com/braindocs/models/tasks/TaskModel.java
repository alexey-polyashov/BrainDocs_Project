package com.braindocs.models.tasks;

import com.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
public class TaskModel {

    @ManyToOne
    @JoinColumn(name="type_id")
    private TaskTypeModel type;

    @Column(name="header")
    private String header;

    @Column(name="content")
    private String content;

    @Column(name="status")
    private Long status;

    @ManyToOne
    @JoinColumn(name="author_id")
    private UserModel author;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<TaskExecutorModel> taskExecutor;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<TaskSubjectModel> taskSubject;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    @ColumnDefault("false")
    private Boolean marked = false;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;

}
