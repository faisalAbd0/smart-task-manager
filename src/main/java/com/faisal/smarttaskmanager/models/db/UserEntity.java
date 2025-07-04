package com.faisal.smarttaskmanager.models.db;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "userEntities")
    private List<TaskEntity> taskEntities;
}
