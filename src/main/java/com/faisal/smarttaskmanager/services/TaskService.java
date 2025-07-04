package com.faisal.smarttaskmanager.services;


import com.faisal.smarttaskmanager.contracts.DateTimeProvider;
import com.faisal.smarttaskmanager.contracts.IdGenerator;
import com.faisal.smarttaskmanager.exceptions.ResourceNotFoundException;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.Task;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.mappers.TaskMapper;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.validators.CreateTaskValidator;
import com.faisal.smarttaskmanager.validators.UpdateTaskValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper mapper;
    private final TaskRepository taskRepository;
    private final CreateTaskValidator createTaskValidator;
    private final UpdateTaskValidator updateTaskValidator;
    private final IdGenerator idGenerator;
    private final DateTimeProvider dateTimeProvider;

    public TaskResourceResponse addTask(CreateTaskRequestResource requestResource) {
        createTaskValidator.validateTaskInfo(requestResource);

        Task task = mapper.toTask(requestResource);
        setSystemValues(task);
        TaskEntity savedEntity = taskRepository.save(task);
        return mapper.toResource(savedEntity);
    }

    private void setSystemValues(Task task) {
        task.setTaskId(idGenerator.generate());
        task.setCreatedAt(dateTimeProvider.now());
    }

    public TaskResourceResponse updateTask(UpdateTaskRequestResource requestResource) {
        updateTaskValidator.validateTaskInfo(requestResource);

        Task task = mapper.toTask(requestResource);
        TaskEntity taskEntity = getIfExists(task.getTaskId());
        updateTaskEntity(taskEntity, task);

        return mapper.toResource(taskEntity);
    }

    private void updateTaskEntity(TaskEntity taskEntity, Task task) {
        taskEntity.setCategory(task.getCategory());
        taskEntity.setCompleted(task.isCompleted());
        taskEntity.setDeadline(task.getDeadline());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setTitle(task.getTitle());
    }

    private TaskEntity getIfExists(String taskId) {
        return taskRepository.getByTaskId(taskId).orElseThrow(ResourceNotFoundException::new);
    }
}
