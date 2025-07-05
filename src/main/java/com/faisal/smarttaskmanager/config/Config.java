package com.faisal.smarttaskmanager.config;


import com.faisal.smarttaskmanager.contracts.DateTimeProvider;
import com.faisal.smarttaskmanager.contracts.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class Config {

    @Bean
    public IdGenerator idGenerator(){
        return () -> "TASK_%s".formatted(LocalDateTime.now().toString());
    }

    @Bean
    public DateTimeProvider dateTimeProvider(){
        return LocalDateTime::now;
    }
}
