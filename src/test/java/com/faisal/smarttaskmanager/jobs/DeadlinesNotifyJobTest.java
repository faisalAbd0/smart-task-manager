package com.faisal.smarttaskmanager.jobs;

import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class DeadlinesNotifyJobTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DeadlinesNotifyJob deadlinesNotifyJob;

    @Captor
    private ArgumentCaptor<List<TaskEntity>> captor;

    @Test
    void givenTasksWithNearDeadlines_whenExecute_thenSetDeadlineNearTrue() {
        List<TaskEntity> taskEntities = List.of(
                TaskEntity.builder().taskId("111").deadlineNear(false).build(),
                TaskEntity.builder().taskId("222").deadlineNear(false).build()
        );

        Mockito.when(taskRepository.getNearDeadlineTasks())
                .thenReturn(taskEntities);


        deadlinesNotifyJob.execute();

        Mockito.verify(taskRepository).updateAll(captor.capture());
        List<TaskEntity> updatedTasks = captor.getValue();

        assertThat(updatedTasks)
                .hasSize(2)
                .allMatch(TaskEntity::isDeadlineNear);
    }

}