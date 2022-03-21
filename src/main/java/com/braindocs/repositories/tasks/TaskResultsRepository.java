package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskResultsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskResultsRepository extends JpaRepository<TaskResultsModel, Long> {

    List<TaskResultsModel> findByMarked(Boolean marked);
    List<TaskResultsModel> findByTaskTypeId(Long taskTypeId);
    Optional<TaskResultsModel> findByIdAndTaskTypeId(Long id, Long typeId);

}
