package com.faisal.smarttaskmanager.models.db;

import com.faisal.smarttaskmanager.models.enums.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TaskEntity {
    @Id @GeneratedValue
    private Long id;


    @Column(unique = true)
    private String taskId;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    private boolean completed = false;
    private boolean deadlineNear = false;

    @ManyToOne
    private UserEntity userEntities;
}
