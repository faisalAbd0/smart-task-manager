package com.faisal.smarttaskmanager.controllers;


import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResourceResponse addTask(@RequestBody CreateTaskRequestResource requestResource) {
        return taskService.addTask(requestResource);
    }

    @PutMapping
    public TaskResourceResponse updateTask(@RequestBody UpdateTaskRequestResource requestResource){
        return taskService.updateTask(requestResource);
    }


}
