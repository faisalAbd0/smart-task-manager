package com.faisal.smarttaskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@EnableScheduling
@SpringBootApplication
public class SmartTaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTaskManagerApplication.class, args);
    }

}
