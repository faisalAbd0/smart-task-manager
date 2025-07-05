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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {


    @Mock private TaskMapper mapper;
    @Mock private TaskRepository taskRepository;
    @Mock private TaskRepositoryJpa taskRepositoryJpa;
    @Mock private CreateTaskValidator createTaskValidator;
    @Mock private UpdateTaskValidator updateTaskValidator;
    @Mock private IdGenerator idGenerator;
    @Mock private DateTimeProvider dateTimeProvider;

    @InjectMocks
    private TaskService taskService;

    @Test
    void givenExistingId_whenFindByTaskId_thenReturnsResponse() {
        String id = "123";
        TaskEntity entity = TaskEntity.builder().taskId(id).build();
        TaskResourceResponse response = TaskResourceResponse.builder().taskId(id).build();

        Mockito.when(taskRepositoryJpa.getByTaskId(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toResource(entity)).thenReturn(response);

        TaskResourceResponse result = taskService.findByTaskId(id);

        assertThat(result).isEqualTo(response);
    }
    @Test
    void givenPageableSpec_whenFindAll_thenMapsEntitiesToResponse() {
        TaskSpec spec = (root, query, criteriaBuilder) -> null;
        Pageable pageable = PageRequest.of(0, 10);

        TaskEntity entity = TaskEntity.builder().taskId("123").build();
        TaskResourceResponse response = TaskResourceResponse.builder().taskId("123").build();
        Page<TaskEntity> entityPage = new PageImpl<>(List.of(entity));

        Mockito.when(taskRepositoryJpa.findAll(spec, pageable)).thenReturn(entityPage);
        Mockito.when(mapper.toResource(entity)).thenReturn(response);

        Page<TaskResourceResponse> result = taskService.findAll(spec, pageable);

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void whenAddTask_thenValidatesMapsAndSaves() {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder().title("test").build();
        Task task = new Task();
        TaskEntity entity = TaskEntity.builder().taskId("abc").build();
        TaskResourceResponse response = TaskResourceResponse.builder().taskId("abc").build();

        Mockito.when(mapper.toTask(request)).thenReturn(task);
        Mockito.when(idGenerator.generate()).thenReturn("abc");
        Mockito.when(dateTimeProvider.now()).thenReturn(LocalDateTime.of(2023, 1, 1, 0, 0));
        Mockito.when(taskRepository.save(task)).thenReturn(entity);
        Mockito.when(mapper.toResource(entity)).thenReturn(response);

        TaskResourceResponse result = taskService.addTask(request);

        assertThat(result).isEqualTo(response);
        assertThat(task.getTaskId()).isEqualTo("abc");
        assertThat(task.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 0, 0));
        Mockito.verify(createTaskValidator).validateTaskInfo(request);
    }
    @Test
    void whenUpdateTask_thenUpdatesEntityCorrectly() {
        String taskId = "id";
        UpdateTaskRequestResource request = UpdateTaskRequestResource.builder().build();
        Task task = new Task();
        task.setTaskId("id");
        task.setTitle("Updated");

        TaskEntity entity = TaskEntity.builder().taskId("id").title("Old").build();
        TaskResourceResponse response = TaskResourceResponse.builder().taskId("id").build();

        Mockito.when(mapper.toTask(request)).thenReturn(task);
        Mockito.when(taskRepository.getByTaskId("id")).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toResource(entity)).thenReturn(response);

        TaskResourceResponse result = taskService.updateTask(taskId, request);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getTitle()).isEqualTo("Updated");
        Mockito.verify(updateTaskValidator).validateTaskInfo(request);
        Mockito.verify(taskRepository).update(entity);
    }
    @Test
    void whenDeleteTask_thenFindsAndDeletesById() {
        TaskEntity entity = TaskEntity.builder().id(99L).taskId("id").build();
        Mockito.when(taskRepository.getByTaskId("id")).thenReturn(Optional.of(entity));

        taskService.deleteTask("id");

        Mockito.verify(taskRepository).deleteByTaskId(99L);
    }
    @Test
    void givenMissingTask_whenFindByTaskId_thenThrowNotFound() {
        Mockito.when(taskRepositoryJpa.getByTaskId("not-found")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findByTaskId("not-found"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}