package com.braindocs.models.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name="task_types")
@Data
@NoArgsConstructor
public class TaskTypeModel {

    @Column(name="typename")
    private String typeName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

}
