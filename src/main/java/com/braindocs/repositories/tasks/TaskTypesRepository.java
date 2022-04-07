package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTypesRepository extends JpaRepository<TaskTypeModel, Long> {

    List<TaskTypeModel> findByMarked(Boolean marked);

}
