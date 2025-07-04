package com.faisal.smarttaskmanager.models.resources;

import com.faisal.smarttaskmanager.models.enums.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private String taskId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Category category;
    private boolean completed;
}
