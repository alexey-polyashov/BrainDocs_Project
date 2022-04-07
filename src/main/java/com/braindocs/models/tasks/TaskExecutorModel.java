package com.braindocs.models.tasks;

import com.braindocs.models.users.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_executors")
@Getter
@Setter
@NoArgsConstructor
public class TaskExecutorModel {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserModel executor;

    @Column(name = "planed_date")
    private LocalDateTime planedDate;

    @Column(name = "date_of_comletion")
    private LocalDateTime dateOfComletion;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "result_id")
    private TaskResultsModel result;

    @Column(name = "status")
    private Long status;//1 ожидает выполнения, 2- в работе, 3- выполнена, 4- отменена, 5- уточнение

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createTime;

}
