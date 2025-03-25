package com.example.Task_management_system_test_task;

import com.example.Task_management_system_test_task.repos.CustomTaskRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = {"com.example.Task_management_system_test_task"},
		repositoryBaseClass = CustomTaskRepositoryImpl.class)
public class TaskManagementSystemTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementSystemTestTaskApplication.class, args);
	}

}
