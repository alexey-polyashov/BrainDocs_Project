package com.braindocs.models.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="task_results")
@Data
@NoArgsConstructor
public class TaskResultsModel {

    @ManyToOne
    @JoinColumn(name="task_type_id")
    private TaskTypeModel taskTypeId;

    @Column(name="resultname")
    private String resultName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
