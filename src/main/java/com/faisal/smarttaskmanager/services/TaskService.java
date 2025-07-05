package com.faisal.smarttaskmanager.services;


import com.faisal.smarttaskmanager.contracts.DateTimeProvider;
import com.faisal.smarttaskmanager.contracts.IdGenerator;
import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.exceptions.ResourceNotFoundException;
import com.faisal.smarttaskmanager.mappers.TaskMapper;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.Task;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.repository.TaskRepositoryJpa;
import com.faisal.smarttaskmanager.repository.specification.TaskSpec;
import com.faisal.smarttaskmanager.validators.CreateTaskValidator;
import com.faisal.smarttaskmanager.validators.UpdateTaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final TaskRepositoryJpa taskRepositoryJpa;

    public TaskResourceResponse findByTaskId(String taskId) {
        TaskEntity taskEntity = taskRepositoryJpa.getByTaskId(taskId)
                .orElseThrow(ResourceNotFoundException::new);
        return mapper.toResource(taskEntity);
    }

    public Page<TaskResourceResponse> findAll(TaskSpec taskSpec, Pageable pageable) {
        Page<TaskEntity> entities = taskRepositoryJpa.findAll(taskSpec, pageable);
        return entities.map(mapper::toResource);
    }

    public TaskResourceResponse addTask(CreateTaskRequestResource requestResource) {
        createTaskValidator.validateTaskInfo(requestResource);

        Task task = mapper.toTask(requestResource);
        setSystemValues(task);
        TaskEntity savedEntity = taskRepository.save(task);
        return mapper.toResource(savedEntity);
    }

    public TaskResourceResponse updateTask(String taskId, UpdateTaskRequestResource requestResource) {
        updateTaskValidator.validateTaskInfo(requestResource);

        Task task = mapper.toTask(requestResource);
        TaskEntity taskEntity = getIfExists(taskId);
        updateTaskEntity(taskEntity, task);
        persistUpdatedEntity(taskEntity);

        return mapper.toResource(taskEntity);
    }

    public void deleteTask(String taskId) {
        TaskEntity taskEntity = getIfExists(taskId);
        taskRepository.deleteByTaskId(taskEntity.getId());
    }

    private TaskEntity getIfExists(String taskId) {
        return taskRepository.getByTaskId(taskId).orElseThrow(ResourceNotFoundException::new);
    }

    private void setSystemValues(Task task) {
        task.setTaskId(idGenerator.generate());
        task.setCreatedAt(dateTimeProvider.now());
    }

    private void persistUpdatedEntity(TaskEntity taskEntity) {
        taskRepository.update(taskEntity);
    }

    private void updateTaskEntity(TaskEntity taskEntity, Task task) {
        taskEntity.setCategory(task.getCategory());
        taskEntity.setCompleted(task.isCompleted());
        taskEntity.setDeadline(task.getDeadline());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setTitle(task.getTitle());
    }


}
