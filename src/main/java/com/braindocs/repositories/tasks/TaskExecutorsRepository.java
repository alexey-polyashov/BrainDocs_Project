package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskExecutorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskExecutorsRepository extends JpaRepository<TaskExecutorModel, Long> {

    List<TaskExecutorModel> findByMarked(Boolean marked);

}
