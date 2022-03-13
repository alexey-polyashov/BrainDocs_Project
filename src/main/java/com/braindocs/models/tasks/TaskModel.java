package com.braindocs.models.tasks;

import com.braindocs.models.users.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
@Getter
@Setter
@NoArgsConstructor
public class TaskModel {

    @ManyToOne
    @JoinColumn(name="type_id")
    private TaskTypeModel type;

    @Column(name="header")
    private String heading;

    @Column(name="content")
    private String content;

    @Column(name="status")
    private Long status;

    @ManyToOne
    @JoinColumn(name="author_id")
    private UserModel author;

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
