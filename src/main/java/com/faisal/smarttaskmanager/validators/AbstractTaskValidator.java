
package com.faisal.smarttaskmanager.validators;


import com.faisal.smarttaskmanager.exceptions.DomainException;
import com.faisal.smarttaskmanager.exceptions.Violation;
import com.faisal.smarttaskmanager.models.enums.Category;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractTaskValidator<T> {

    public void validateTaskInfo(T requestResource) {
        Set<Violation> violations = new HashSet<>();

        titleValidation(requestResource, violations);
        descriptionValidation(requestResource, violations);
        deadLineValidation(requestResource, violations);
        categoryValidation(requestResource, violations);

        if (!violations.isEmpty()){
            throw new DomainException(violations);
        }
    }

    private void categoryValidation(T requestResource, Set<Violation> violations) {
        if (Objects.isNull(getCategory(requestResource))){
            violations.add(Violation.of("category", "category.can.not.be.null"));
        }
    }

    private void deadLineValidation(T requestResource, Set<Violation> violations) {
        if (Objects.isNull(getDeadline(requestResource))){
            violations.add(Violation.of("deadline", "deadline.can.not.be.null"));
        }

        if (getDeadline(requestResource).isBefore(LocalDateTime.now())){
            violations.add(Violation.of("deadline", "deadline.can.not.be.old.date.time"));
        }

    }

    private void descriptionValidation(T requestResource, Set<Violation> violations) {
        if (StringUtils.isBlank(getDescription(requestResource))){
            violations.add(Violation.of("description", "description.can.not.be.blank"));
        }
    }

    private void titleValidation(T requestResource, Set<Violation> violations) {
        if (StringUtils.isBlank(getTitle(requestResource))){
            violations.add(Violation.of("title", "title.can.not.be.blank"));
        }
    }

    protected abstract String getTitle(T request);

    protected abstract String getDescription(T request);

    protected abstract Category getCategory(T request);

    protected abstract LocalDateTime getDeadline(T request);

}
