package com.braindocs.models.tasks;

import com.braindocs.models.users.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Date;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="task_executors")
@Getter
@Setter
@NoArgsConstructor
public class TaskExecutorModel {

    @ManyToOne
    @JoinColumn(name="type_id")
    private TaskTypeModel type;

    @ManyToOne
    @JoinColumn(name="executor_id")
    private UserModel executor;

    @Column(name="planed_date")
    private Date planedDate;

    @Column(name="date_of_comletion")
    private Date dateOfComletion;

    @Column(name="comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name="result_id")
    private TaskResultsModel result;

    @Column(name="status")
    private Long status;

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
