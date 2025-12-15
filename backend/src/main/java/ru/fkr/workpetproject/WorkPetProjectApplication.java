package ru.fkr.workpetproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkPetProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkPetProjectApplication.class, args);
    }

}
