package com.braindocs.models.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "task_results")
@Getter
@Setter
@NoArgsConstructor
public class TaskResultsModel {

    @ManyToOne
    @JoinColumn(name = "task_type_id")
    private TaskTypeModel taskType;

    @Column(name = "resultname")
    private String resultName;

    @Column(name = "result_type")
    private Integer resultType;//1-positive, 2-withComments, 3-negative, 4-other

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    @ColumnDefault("false")
    private Boolean marked = false;
}
