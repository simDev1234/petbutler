package com.example.petbutler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories("com.example.petbutler.persist")
@EnableTransactionManagement
public class PetbutlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetbutlerApplication.class, args);
	}

}
