package com.braindocs.repositories.tasks;

import com.braindocs.models.tasks.TaskCommentModel;
import com.braindocs.models.tasks.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskCommentsRepository extends JpaRepository<TaskCommentModel, Long> {

    List<TaskCommentModel> findByTask(TaskModel taskModel);

    Optional<TaskCommentModel> findByTaskAndId(TaskModel task, Long commentId);

    void deleteByTaskAndId(TaskModel task, Long commentId);
}
