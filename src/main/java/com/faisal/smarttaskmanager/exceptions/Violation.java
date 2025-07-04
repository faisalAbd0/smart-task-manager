package com.faisal.smarttaskmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Violation {
    private final String violator;
    private final String errorMessage;
    
    public static Violation of(String violator, String errorMessage) {
        return new Violation(violator, errorMessage);
    }

}
