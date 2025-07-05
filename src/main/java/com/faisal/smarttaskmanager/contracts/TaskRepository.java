package com.faisal.smarttaskmanager.contracts;

import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    TaskEntity save(Task task);

    Optional<TaskEntity> getByTaskId(String taskId);

    void deleteByTaskId(Long taskId);

    List<TaskEntity> getNearDeadlineTasks();

    void updateAll(List<TaskEntity> taskEntities);

    void update(TaskEntity taskEntity);

}
