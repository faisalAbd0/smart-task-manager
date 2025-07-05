package com.faisal.smarttaskmanager.repository.specification;


import com.faisal.smarttaskmanager.models.db.TaskEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.LessThan;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "category", spec = EqualIgnoreCase.class),
        @Spec(path = "deadline-before", spec = LessThan.class)
})
public interface TaskSpec extends Specification<TaskEntity> {
}
