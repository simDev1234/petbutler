package com.example.petbutler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories("com.example.petbutler.persist")
@EnableTransactionManagement
@EnableBatchProcessing
@EnableScheduling
public class PetbutlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetbutlerApplication.class, args);
	}

}
