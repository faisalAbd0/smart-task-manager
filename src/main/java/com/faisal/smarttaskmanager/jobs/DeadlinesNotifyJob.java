package com.faisal.smarttaskmanager.jobs;

import com.faisal.smarttaskmanager.contracts.TaskRepository;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadlinesNotifyJob {

    private final TaskRepository taskRepository;

    @Scheduled(fixedDelayString = "600000")
    void execute() {
        log.debug("deadline checking job starts ...");

        List<TaskEntity> taskEntities = taskRepository
                .getNearDeadlineTasks()
                .stream().peek(taskEntity -> {
                    taskEntity.setDeadlineNear(true);
                    log.info("task {} deadline is one day left", taskEntity);
                })
                .toList();

        taskRepository.updateAll(taskEntities);
        log.debug("deadline checking job ends ...");
    }
}
