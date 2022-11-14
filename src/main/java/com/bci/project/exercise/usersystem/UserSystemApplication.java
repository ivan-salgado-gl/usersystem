package com.bci.project.exercise.usersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSystemApplication.class, args);
	}

}
