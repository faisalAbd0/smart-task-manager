package com.faisal.smarttaskmanager.controllers;


import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResourceResponse findByTaskId(@PathVariable("taskId") String taskId){
        return taskService.findByTaskId(taskId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResourceResponse> findAllTasks(){
        return taskService.findAll();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResourceResponse addTask(@RequestBody CreateTaskRequestResource requestResource) {
        return taskService.addTask(requestResource);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskResourceResponse updateTask(@RequestBody UpdateTaskRequestResource requestResource) {
        return taskService.updateTask(requestResource);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(String taskId){
        taskService.deleteTask(taskId);
    }
}
