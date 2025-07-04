package com.faisal.smarttaskmanager.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class DomainException extends RuntimeException{
    private final Set<Violation> violations;
}
