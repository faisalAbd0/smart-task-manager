package com.faisal.smarttaskmanager.exceptions;

import java.util.Objects;

public class Violation {
    private final String violator;
    private final String errorMessage;

    public Violation(String violator, String errorMessage) {
        this.violator = violator;
        this.errorMessage = errorMessage;
    }

    public static Violation of(String violator, String errorMessage) {
        return new Violation(violator, errorMessage);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Violation violation = (Violation) o;
        return Objects.equals(violator, violation.violator) && Objects.equals(errorMessage, violation.errorMessage);
    }

    public String getViolator() {
        return violator;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(violator);
        result = 31 * result + Objects.hashCode(errorMessage);
        return result;
    }


    @Override
    public String toString() {
        return "Violation { " +
                "violator= " + violator +
                ", errorMessage= " + errorMessage +
                " }";
    }
}
