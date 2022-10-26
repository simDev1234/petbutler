package com.example.petbutler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PetbutlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetbutlerApplication.class, args);
	}

}
