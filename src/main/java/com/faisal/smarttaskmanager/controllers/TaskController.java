package com.faisal.smarttaskmanager.controllers;


import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.repository.specification.TaskSpec;
import com.faisal.smarttaskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResourceResponse findByTaskId(@PathVariable("id") String taskId) {
        return taskService.findByTaskId(taskId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResourceResponse> findAllTasks(
            TaskSpec spec,
            @SortDefault(sort = "deadline", direction = Sort.Direction.DESC) Pageable pageable) {

        return taskService.findAll(spec, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResourceResponse addTask(@RequestBody CreateTaskRequestResource requestResource) {
        return taskService.addTask(requestResource);
    }

    @PutMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public TaskResourceResponse updateTask(@PathVariable("id") String taskId,
                                           @RequestBody UpdateTaskRequestResource requestResource) {
        return taskService.updateTask(taskId, requestResource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable("id") String taskId) {
        taskService.deleteTask(taskId);
    }
}
