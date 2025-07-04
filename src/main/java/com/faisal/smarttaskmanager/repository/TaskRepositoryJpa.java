package com.faisal.smarttaskmanager.repository;

import com.faisal.smarttaskmanager.models.db.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepositoryJpa extends JpaRepository<TaskEntity, Long>{
    Optional<TaskEntity> getByTaskId(String taskId);
}
