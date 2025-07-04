package com.faisal.smarttaskmanager.mappers;


import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import com.faisal.smarttaskmanager.models.resources.Task;
import com.faisal.smarttaskmanager.models.resources.TaskResourceResponse;
import com.faisal.smarttaskmanager.models.db.TaskEntity;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResourceResponse toResource(TaskEntity requestResource);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completed", ignore = true)
    Task toTask(CreateTaskRequestResource requestResource);

    @Mapping(target = "deadlineNear", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userEntities", ignore = true)
    TaskEntity toEntity(Task task);


    @Mapping(target = "createdAt", ignore = true)
    Task toTask(UpdateTaskRequestResource requestResource);

    default List<TaskResourceResponse> toResourceList(List<TaskEntity> taskEntities){
        return taskEntities
                .stream()
                .map(this::toResource)
                .toList();
    }


}
