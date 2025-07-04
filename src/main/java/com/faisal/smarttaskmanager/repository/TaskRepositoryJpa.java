package com.faisal.smarttaskmanager.repository;

import com.faisal.smarttaskmanager.models.db.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryJpa extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    Optional<TaskEntity> getByTaskId(String taskId);

    List<TaskEntity> findAllByDeadlineAfter(LocalDateTime deadlineAfter);

    Page<TaskEntity> findAll(Specification<TaskEntity> spec , Pageable pageable);
}
