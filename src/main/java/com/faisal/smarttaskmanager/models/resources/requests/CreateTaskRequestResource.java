package com.faisal.smarttaskmanager.models.resources.requests;

import com.faisal.smarttaskmanager.models.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestResource {

    private String taskId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Category category;

}
