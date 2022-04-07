package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskExecutorModel;
import com.braindocs.models.tasks.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskExecutorsRepository extends JpaRepository<TaskExecutorModel, Long>,
        JpaSpecificationExecutor<TaskExecutorModel> {

    List<TaskExecutorModel> findByTask(TaskModel task);

    Optional<TaskExecutorModel> findByTaskAndId(TaskModel task, Long exId);

    void deleteByTaskAndId(TaskModel task, Long exId);

    @Query("SELECT ex FROM TaskExecutorModel ex WHERE ex.task = ?1 AND ex.status IN('1', '2', '5')")
    List<TaskExecutorModel> findActiveExecutorsInTask(TaskModel task);

}
