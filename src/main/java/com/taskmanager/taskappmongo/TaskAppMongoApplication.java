package com.taskmanager.taskappmongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TaskAppMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskAppMongoApplication.class, args);
	}

}
