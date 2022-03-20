package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskResultsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskResultsRepository extends JpaRepository<TaskResultsModel, Long> {

    List<TaskResultsModel> findByMarked(Boolean marked);
    List<TaskResultsModel> findByTaskTypeId(Long taskTypeId);

}
