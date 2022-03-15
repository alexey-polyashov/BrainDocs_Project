package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<TaskModel, Long>,
        JpaSpecificationExecutor<TaskModel> {

    Page<TaskModel> findByMarked(Boolean marked, PageRequest of);

}
