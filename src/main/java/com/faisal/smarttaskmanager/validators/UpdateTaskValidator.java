package com.faisal.smarttaskmanager.validators;

import com.faisal.smarttaskmanager.models.enums.Category;
import com.faisal.smarttaskmanager.models.resources.requests.UpdateTaskRequestResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateTaskValidator extends AbstractTaskValidator<UpdateTaskRequestResource> {
    @Override
    protected String getTitle(UpdateTaskRequestResource request) {
        return request.getTitle();
    }

    @Override
    protected String getDescription(UpdateTaskRequestResource request) {
        return request.getDescription();
    }

    @Override
    protected Category getCategory(UpdateTaskRequestResource request) {
        return request.getCategory();
    }

    @Override
    protected LocalDateTime getDeadline(UpdateTaskRequestResource request) {
        return request.getDeadline();
    }
}
