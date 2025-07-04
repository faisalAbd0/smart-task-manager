package com.faisal.smarttaskmanager.validators;

import com.faisal.smarttaskmanager.models.enums.Category;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CreateTaskValidator extends AbstractTaskValidator<CreateTaskRequestResource> {
    @Override
    protected String getTitle(CreateTaskRequestResource request) {
        return request.getTitle();
    }

    @Override
    protected String getDescription(CreateTaskRequestResource request) {
        return request.getDescription();
    }

    @Override
    protected Category getCategory(CreateTaskRequestResource request) {
        return request.getCategory();
    }

    @Override
    protected LocalDateTime getDeadline(CreateTaskRequestResource request) {
        return request.getDeadline();
    }
}
