package com.faisal.smarttaskmanager.repository;

import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.mappers.TaskMapper;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferRepositoryAdaptorTest {

    @Mock private TaskRepositoryJpa taskRepositoryJpa;
    @Mock private TaskMapper taskMapper;
    @InjectMocks private TransferRepositoryAdaptor adaptor;


    @Test
    void givenTask_whenSave_thenShouldMapAndSave() {
        Task task = new Task();
        TaskEntity entity = TaskEntity.builder().taskId("123").build();

        when(taskMapper.toEntity(task)).thenReturn(entity);
        when(taskRepositoryJpa.save(entity)).thenReturn(entity);

        TaskEntity result = adaptor.save(task);

        assertThat(result).isEqualTo(entity);
        verify(taskMapper).toEntity(task);
        verify(taskRepositoryJpa).save(entity);
    }

    @Test
    void givenTaskId_whenGetByTaskId_thenReturnOptionalEntity() {
        TaskEntity entity = TaskEntity.builder().taskId("abc").build();
        when(taskRepositoryJpa.getByTaskId("abc")).thenReturn(Optional.of(entity));

        Optional<TaskEntity> result = adaptor.getByTaskId("abc");

        assertThat(result).contains(entity);
        verify(taskRepositoryJpa).getByTaskId("abc");
    }

    @Test
    void givenTaskId_whenDelete_thenShouldCallJpaDelete() {
        adaptor.deleteByTaskId(1L);

        verify(taskRepositoryJpa).deleteById(1L);
    }

    @Test
    void whenGetNearDeadlineTasks_thenReturnFromJpa() {
        List<TaskEntity> expected = List.of(TaskEntity.builder().taskId("t1").build());
        when(taskRepositoryJpa.findAllByDeadlineAfter(any())).thenReturn(expected);

        List<TaskEntity> result = adaptor.getNearDeadlineTasks();

        assertThat(result).isEqualTo(expected);
        verify(taskRepositoryJpa).findAllByDeadlineAfter(any());
    }

    @Test
    void whenUpdateAll_thenCallsSaveAll() {
        List<TaskEntity> list = List.of(TaskEntity.builder().taskId("1").build());

        adaptor.updateAll(list);

        verify(taskRepositoryJpa).saveAll(list);
    }

    @Test
    void givenTaskWithNullId_whenUpdate_thenLoadsAndSetsIdBeforeSave() {
        TaskEntity input = TaskEntity.builder().taskId("t1").build(); // id null
        TaskEntity existing = TaskEntity.builder().taskId("t1").id(10L).build();

        when(taskRepositoryJpa.getByTaskId("t1")).thenReturn(Optional.of(existing));
        when(taskRepositoryJpa.save(any())).thenReturn(existing);

        adaptor.update(input);

        assertThat(input.getId()).isEqualTo(10L);
        verify(taskRepositoryJpa).save(input);
    }

    @Test
    void givenTaskWithId_whenUpdate_thenDirectSave() {
        TaskEntity entity = TaskEntity.builder().id(99L).taskId("id").build();

        when(taskRepositoryJpa.save(entity)).thenReturn(entity);

        adaptor.update(entity);

        verify(taskRepositoryJpa).save(entity);
    }

    @Test
    void givenUnknownTaskId_whenUpdate_thenThrows() {
        TaskEntity entity = TaskEntity.builder().taskId("missing").build();

        when(taskRepositoryJpa.getByTaskId("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adaptor.update(entity))
                .isInstanceOf(IllegalStateException.class);
    }
}
