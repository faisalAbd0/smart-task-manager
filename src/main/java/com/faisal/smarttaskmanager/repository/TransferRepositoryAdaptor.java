package com.faisal.smarttaskmanager.repository;


import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.mappers.TaskMapper;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.Task;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    @Override
    public void deleteByTaskId(Long taskId) {
        taskRepositoryJpa.deleteById(taskId);
    }

    @Override
    public List<TaskEntity> getNearDeadlineTasks() {
        return taskRepositoryJpa.findAllByDeadlineAfter(LocalDateTime.now().plusDays(1));
    }

    @Override
    public void updateAll(List<TaskEntity> taskEntities) {
        taskRepositoryJpa.saveAll(taskEntities);
    }

    @Override
    public void update(TaskEntity taskEntity) {
        if (Objects.isNull(taskEntity.getId())) {
            TaskEntity savedTask =
                    taskRepositoryJpa.getByTaskId(taskEntity.getTaskId())
                            .orElseThrow(IllegalStateException::new);
            taskEntity.setId(savedTask.getId());
        }

        taskRepositoryJpa.save(taskEntity);
    }

}
