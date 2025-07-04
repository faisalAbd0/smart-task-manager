package com.faisal.smarttaskmanager.repository;

import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryJpa extends JpaRepository<TaskEntity, Long>{
    Optional<TaskEntity> getByTaskId(String taskId);

    List<TaskEntity> findAllByDeadlineAfter(LocalDateTime deadlineAfter);

    TaskEntity findByTaskId(String taskId);
}
