package com.faisal.smarttaskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.faisal.smarttaskmanager.models.enums.Category;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import com.faisal.smarttaskmanager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetById_thenReturnTask() throws Exception {
        String taskId = UUID.randomUUID().toString();
        String title = "any title";
        TaskResourceResponse response = TaskResourceResponse.builder()
                .taskId(taskId)
                .title(title)
                .build();

        Mockito.when(taskService.findByTaskId(taskId)).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void whenFindAllTasks_thenReturnPageOfTaskResponses() throws Exception {
        TaskResourceResponse task1 = TaskResourceResponse.builder()
                .taskId(UUID.randomUUID().toString())
                .title("Task One")
                .category(Category.WORK)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        TaskResourceResponse task2 = TaskResourceResponse.builder()
                .taskId(UUID.randomUUID().toString())
                .title("Task Two")
                .category(Category.PERSONAL)
                .deadline(LocalDateTime.now().plusDays(2))
                .build();

        Page<TaskResourceResponse> page = new PageImpl<>(List.of(task1, task2));

        Mockito.when(taskService.findAll(any(), any())).thenReturn(page);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Task One"))
                .andExpect(jsonPath("$.content[1].title").value("Task Two"));
    }

    @Test
    void givenCreateTaskRequest_whenPostTask_thenReturnCreatedTask() throws Exception {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder()
                .title("New Task")
                .deadline(LocalDateTime.now().plusDays(1))
                .category(Category.WORK)
                .build();

        TaskResourceResponse response = TaskResourceResponse.builder()
                .title(request.getTitle())
                .build();

        Mockito.when(taskService.addTask(any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void givenUpdatedTaskRequest_whenPutTask_thenReturnUpdatedTask() throws Exception {
        String taskId = UUID.randomUUID().toString();
        UpdateTaskRequestResource request = UpdateTaskRequestResource.builder()
                .title("Updated Task")
                .deadline(LocalDateTime.now().plusDays(2))
                .category(Category.PERSONAL)
                .build();

        TaskResourceResponse response = TaskResourceResponse.builder()
                .taskId(taskId)
                .title(request.getTitle())
                .build();

        Mockito.when(taskService.updateTask(any(), any())).thenReturn(response);

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{id}", taskId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.taskId").value(response.getTaskId()))
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void whenDeleteTask_thenReturnOk() throws Exception {
        String taskId = UUID.randomUUID().toString();

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{id}",taskId))
                .andExpect(status().isOk());

        Mockito.verify(taskService).deleteTask(taskId);
    }
}
