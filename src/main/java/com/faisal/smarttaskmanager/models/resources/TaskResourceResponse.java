package com.faisal.smarttaskmanager.models.resources;


import com.faisal.smarttaskmanager.models.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResourceResponse {

    private String taskId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Category category;
    private boolean completed;
}
