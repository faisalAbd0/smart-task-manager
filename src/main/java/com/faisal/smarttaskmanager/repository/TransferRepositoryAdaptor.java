package com.faisal.smarttaskmanager.repository;


import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.mappers.TaskMapper;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferRepositoryAdaptor implements TaskRepository {

    private final TaskRepositoryJpa taskRepositoryJpa;
    private final TaskMapper taskMapper;

    @Override
    public TaskEntity save(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        return taskRepositoryJpa.save(entity);
    }

    @Override
    public Optional<TaskEntity> getByTaskId(String taskId) {
        return taskRepositoryJpa.getByTaskId(taskId);
    }
}
