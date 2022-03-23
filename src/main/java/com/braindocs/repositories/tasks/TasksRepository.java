package com.braindocs.repositories.tasks;

import com.braindocs.models.documents.DocumentModel;
import com.braindocs.models.tasks.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<TaskModel, Long>,
        JpaSpecificationExecutor<TaskModel> {

    Page<TaskModel> findByMarked(Boolean marked, PageRequest of);

    @Query("SELECT task FROM TaskModel task JOIN task.subjects subj WHERE subj in (Select doc From DocumentModel doc Where doc.id = ?1)")
    List<TaskModel> findForDocument(Long docId);
}
