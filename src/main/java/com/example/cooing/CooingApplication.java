package com.example.cooing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CooingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CooingApplication.class, args);
	}

}
