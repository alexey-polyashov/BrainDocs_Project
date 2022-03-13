package com.braindocs.models.tasks;

import com.braindocs.models.users.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="task_executors")
@Getter
@Setter
@NoArgsConstructor
public class TaskCommentModel {

    @ManyToOne
    @JoinColumn(name="task_id")
    private TaskModel task;

    @ManyToOne
    @JoinColumn(name="author_id")
    private UserModel author;

    @Column(name="comment")
    private String comment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;

}
