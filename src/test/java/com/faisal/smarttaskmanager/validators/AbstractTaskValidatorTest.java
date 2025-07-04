package com.faisal.smarttaskmanager.validators;

import com.faisal.smarttaskmanager.exceptions.DomainException;
import com.faisal.smarttaskmanager.exceptions.Violation;
import com.faisal.smarttaskmanager.models.enums.Category;
import com.faisal.smarttaskmanager.models.resources.requests.CreateTaskRequestResource;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class AbstractTaskValidatorTest {

    private final AbstractTaskValidator<CreateTaskRequestResource> validator = new AbstractTaskValidator<>() {
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
    };

    @Test
    void givenValidRequest_whenValidateTaskInfo_thenNoException() {
        CreateTaskRequestResource validRequest = CreateTaskRequestResource.builder()
                .title("Valid Task")
                .description("This is a valid task.")
                .category(Category.WORK)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        assertThatCode(() -> validator.validateTaskInfo(validRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void givenMissingTitle_whenValidateTaskInfo_thenThrowsDomainException() {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder()
                .title(" ")
                .description("desc")
                .category(Category.WORK)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        DomainException ex = catchException(() -> validator.validateTaskInfo(request));
        assertViolation(ex.getViolations(), "title", "title.can.not.be.blank");
    }

    @Test
    void givenOldDeadline_whenValidateTaskInfo_thenThrowsDomainException() {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder()
                .title("task")
                .description("desc")
                .category(Category.WORK)
                .deadline(LocalDateTime.now().minusDays(1))
                .build();

        DomainException ex = catchException(() -> validator.validateTaskInfo(request));
        assertViolation(ex.getViolations(), "deadline", "deadline.can.not.be.old.date.time");
    }

    @Test
    void givenNullCategory_whenValidateTaskInfo_thenThrowsDomainException() {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder()
                .title("task")
                .description("desc")
                .category(null)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        DomainException ex = catchException(() -> validator.validateTaskInfo(request));
        assertViolation(ex.getViolations(), "category", "category.can.not.be.null");
    }

    @Test
    void givenBlankDescription_whenValidateTaskInfo_thenThrowsDomainException() {
        CreateTaskRequestResource request = CreateTaskRequestResource.builder()
                .title("task")
                .description(" ")
                .category(Category.WORK)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        DomainException ex = catchException(() -> validator.validateTaskInfo(request));
        assertViolation(ex.getViolations(), "description", "description.can.not.be.blank");
    }

    private DomainException catchException(Runnable action) {
        try {
            action.run();
        } catch (DomainException ex) {
            return ex;
        }
        throw new AssertionError("Expected DomainException was not thrown");
    }


    private void assertViolation(Set<Violation> violations, String field, String message) {
        assertThat(violations)
                .anyMatch(v -> v.getViolator().equals(field) && v.getErrorMessage().equals(message));
    }
}
