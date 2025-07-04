package com.faisal.smarttaskmanager.models.resources.requests;

import com.faisal.smarttaskmanager.models.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTaskRequestResource {

    private String taskId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;
    private Category category;
    private boolean completed;

}
